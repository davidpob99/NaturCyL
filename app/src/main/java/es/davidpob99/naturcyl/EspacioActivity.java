/*
 * Aplicación para consultar los espacios naturales de la comunidad
 * autónoma de Castilla y León, así como sus equipamientos y
 * posibilidades.
 *
 * Copyright (C) 2019  David Población Criado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package es.davidpob99.naturcyl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.FlickrPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class EspacioActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 12;
    private static final String URL_ORTOFOTO = "http://www.idecyl.jcyl.es/IGCyL/services/PaisajeCubierta/Ortofoto/MapServer/WMSServer?request=GetCapabilities&service=WMS";

    protected static EspacioNatural espacioNatural;
    private MapView map;
    private IMapController mapController;
    private int posicion;
    private boolean satelite = false;
    private String FLICKR_APIKEY;

    private TextView espacioTipo;
    private TextView espacioFecha;
    private ImageView espacioFoto;
    private FloatingActionButton fab;
    private ImageButton imageButton;
    private CarouselView carouselView;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espacio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Type tipoListaUrls = new TypeToken<ArrayList<URL>>() {
                }.getType();
                ArrayList<URL> urls = gson.fromJson(leerUrls(getApplicationContext()), tipoListaUrls);
                URL url = null;
                for (URL u : urls) {
                    if (u.getCodigo().equals(espacioNatural.getCodigo())) {
                        url = u;
                        break;
                    }
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url.getUrlJcyl()));
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FLICKR_APIKEY = getString(R.string.FLICKR_APIKEY);
        carouselView = findViewById(R.id.espacio_carousel);
        constraintLayout = findViewById(R.id.espacio_layout);

        imageButton = findViewById(R.id.espacio_capa_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!satelite) {
                    MapBoxTileSource tileSource = new MapBoxTileSource("MapBoxSatelliteLabelled", 1, 19, 256, ".png");
                    //option 1, load your settings from the manifest
                    tileSource.setAccessToken(getResources().getString(R.string.MAPBOX_ACCESS_TOKEN));
                    tileSource.setMapboxMapid(getResources().getString(R.string.MAPBOX_MAPID));
                    map.setTileSource(tileSource);
                    satelite = true;
                } else {
                    map.setTileSource(TileSourceFactory.MAPNIK);
                    satelite = false;
                }
            }
        });

        /*obtenerFavoritos();
        cambiarImagen();*/

        // Obtener de main
        String s = getIntent().getStringExtra("posicion");
        posicion = Integer.valueOf(s);

        // Inicializar mapa
        map = findViewById(R.id.espacio_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(ZOOM_MAPA);
        mapController.setCenter(calcularPuntoMedio());

        // Dibujar espacio
        map.getOverlayManager().add(espacioNatural.getPoligonoCoordenadas());

        obtenerImagenes();

        // Inicializar texto e imagen
        espacioTipo = findViewById(R.id.espacio_tipo);
        espacioFecha = findViewById(R.id.espacio_descripcion);
        espacioFoto = findViewById(R.id.espacio_foto);

        setTitle(espacioNatural.getNombre());
        espacioTipo.setText(espacioNatural.getTipoDeclaracion());
        espacioFecha.setText(espacioNatural.getFechaDeclaracion());
        Picasso.get().load(EspacioNatural.URL_IMG_BASE + espacioNatural.getImagen()).into(espacioFoto);

        Set<Integer> itemsDisponibles = comrobarItems();

        // Cargar RecyclerView
        RecyclerView rv = findViewById(R.id.espacio_item_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        final ArrayList<EItem> listaItemsDisponibles = Utilidades.crearItems(itemsDisponibles);
        RVAdapterEspacioItem adapter = new RVAdapterEspacioItem(listaItemsDisponibles, new RVClickListenerEspacio() {
            @Override
            public void onClickItem(View v, int position) {
                ListaItemsActivity.espacioNatural = espacioNatural;
                Intent myIntent = new Intent(EspacioActivity.this, ListaItemsActivity.class);
                myIntent.putExtra("posicion", String.valueOf(listaItemsDisponibles.get(position).getPosicion()));
                startActivity(myIntent);
            }
        });
        rv.setAdapter(adapter);


    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_espacio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accion_leyenda) {
            Intent myIntent = new Intent(EspacioActivity.this, TextoActivity.class);
            myIntent.putExtra("accion", "leyenda");
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Dado el espacio natural que es un atributo de la clase calcula el punto medio
     * del polígono que forman sus coordenadas
     *
     * https://stackoverflow.com/questions/2792443/finding-the-centroid-of-a-polygon
     *
     * @return coordenadas del punto medio del polígono
     */
    public GeoPoint calcularPuntoMedio() {
        /*double x = 0.;
        double y = 0.;
        int cont = espacioNatural.getCoordenadas().size();
        for (int i = 0; i < cont - 1; i++) {
            final GeoPoint point = espacioNatural.getCoordenadas().get(i);
            x += point.getLatitude();
            y += point.getLongitude();
        }

        x = x / cont;
        y = y / cont;

        return new GeoPoint(x, y);*/

        double[] centroid = {0, 0};
        double[][] vertices = new double[2][espacioNatural.getCoordenadas().size()];
        for (int j = 0; j < espacioNatural.getCoordenadas().size(); j++) {
            vertices[0][j] = espacioNatural.getCoordenadas().get(j).getLatitude();
            vertices[1][j] = espacioNatural.getCoordenadas().get(j).getLongitude();
        }
        double signedArea = 0.0;
        double x0 = 0.0; // Current vertex X
        double y0 = 0.0; // Current vertex Y
        double x1 = 0.0; // Next vertex X
        double y1 = 0.0; // Next vertex Y
        double a = 0.0;  // Partial signed area

        // For all vertices except last
        int i = 0;
        for (i = 0; i < espacioNatural.getCoordenadas().size() - 1; ++i) {
            x0 = vertices[0][i];
            y0 = vertices[1][i];
            x1 = vertices[0][i + 1];
            y1 = vertices[1][i + 1];
            a = x0 * y1 - x1 * y0;
            signedArea += a;
            centroid[0] += (x0 + x1) * a;
            centroid[1] += (y0 + y1) * a;
        }

        // Do last vertex separately to avoid performing an expensive
        // modulus operation in each iteration.
        x0 = vertices[0][i];
        y0 = vertices[1][i];
        x1 = vertices[0][0];
        y1 = vertices[1][0];
        a = x0 * y1 - x1 * y0;
        signedArea += a;
        centroid[0] += (x0 + x1) * a;
        centroid[1] += (y0 + y1) * a;

        signedArea *= 0.5;
        centroid[0] /= (6.0 * signedArea);
        centroid[1] /= (6.0 * signedArea);

        return new GeoPoint(centroid[0], centroid[1]);
    }

    private Set comrobarItems() {
        Set<Integer> set = new HashSet();

        for (int i = 0; i < Utilidades.nombresFicheros.length; i++) {
            try {
                BufferedReader bf = new BufferedReader(new FileReader(getFilesDir() + "/" + Utilidades.nombresFicheros[i] + ".kml"));
                String linea;
                while ((linea = bf.readLine()) != null) {
                    if (linea.contains(espacioNatural.getCodigo())) {
                        set.add(i);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return set;
    }

    private void obtenerImagenes() {
        try {
            final ArrayList<POI> fotos = new ObtenerImagenes().execute().get();
            if (fotos != null) {
                carouselView.setPageCount(fotos.size());
                carouselView.setImageListener(new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {
                        Picasso.get().load(fotos.get(position).mThumbnailPath.replace("s.jpg", "h.jpg")).into(imageView);
                    }
                });
                carouselView.setImageClickListener(new ImageClickListener() {
                    @Override
                    public void onClick(int position) {
                        String fotoBuenaDefinicion = fotos.get(position).mThumbnailPath.replace("s.jpg", "h.jpg");
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(fotoBuenaDefinicion), "image/*");
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(EspacioActivity.this, "No se han podido descargar las fotografías. Compruebe su conexión a Internet", Toast.LENGTH_LONG).show();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class ObtenerImagenes extends AsyncTask<Void, Void, ArrayList<POI>> {
        @Override
        protected ArrayList<POI> doInBackground(Void... voids) {
            FlickrPOIProvider poiProvider = new FlickrPOIProvider(FLICKR_APIKEY);
            BoundingBox bb = BoundingBox.fromGeoPoints(espacioNatural.getCoordenadas());
            ArrayList<POI> fotos = poiProvider.getPOIInside(bb, 20);
            return fotos;
        }
    }

    private String leerUrls(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.urls);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

class URL {
    String codigo;
    String urlJcyl;
    String urlWikipedia;

    public URL() {
    }

    public URL(String codigo, String urlJcyl, String urlWikipedia) {
        this.codigo = codigo;
        this.urlJcyl = urlJcyl;
        this.urlWikipedia = urlWikipedia;
    }

    @Override
    public String toString() {
        return "URL{" +
                "codigo='" + codigo + '\'' +
                ", urlJcyl='" + urlJcyl + '\'' +
                ", urlWikipedia='" + urlWikipedia + '\'' +
                '}';
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUrlJcyl() {
        return urlJcyl;
    }

    public void setUrlJcyl(String urlJcyl) {
        this.urlJcyl = urlJcyl;
    }

    public String getUrlWikipedia() {
        return urlWikipedia;
    }

    public void setUrlWikipedia(String urlWikipedia) {
        this.urlWikipedia = urlWikipedia;
    }
}
