/*
 * Aplicación para consultar los espacios naturales de la comunidad
 * autónoma de Castilla y León, así como sus equipamientos y
 * posibilidades.
 *
 * Copyright (C) 2019  David Población Criado
 *
 * Este programa es software libre: puede redistribuirlo y/o modificarlo bajo
 * los términos de la Licencia General Pública de GNU publicada por la Free
 * Software Foundation, ya sea la versión 3 de la Licencia, o (a su elección)
 * cualquier versión posterior.\n\n
 *
 * Este programa se distribuye con la esperanza de que sea útil pero SIN
 * NINGUNA GARANTÍA; incluso sin la garantía implícita de MERCANTIBILIDAD o
 * CALIFICADA PARA UN PROPÓSITO EN PARTICULAR. Vea la Licencia General Pública
 * de GNU para más detalles.\n\n
 *
 * Usted ha debido de recibir una copia de la Licencia General Pública
 * de GNU junto con este programa. Si no, vea http://www.gnu.org/licenses/
 */

package es.davidpob99.naturcyl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class EspacioActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 12;

    static EspacioNatural espacioNatural;
    private MapView map;
    private boolean satelite = false;
    private String FLICKR_APIKEY;

    private CarouselView carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espacio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Type tipoListaUrls = new TypeToken<ArrayList<URLEspacio>>() {
                }.getType();
                ArrayList<URLEspacio> urls = gson.fromJson(leerUrls(getApplicationContext()), tipoListaUrls);
                for (final URLEspacio u : Objects.requireNonNull(urls)) {
                    if (u.getCodigo().equals(espacioNatural.getCodigo())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EspacioActivity.this);
                        builder.setTitle("Seleccione enlace")
                                .setNegativeButton("JCyL", new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(u.getUrlJcyl()));
                                        startActivity(intent);
                                    }
                                })
                                .setPositiveButton("Wikipedia", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(u.getUrlWikipedia()));
                                        startActivity(intent);
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }
                }
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FLICKR_APIKEY = getString(R.string.FLICKR_APIKEY);
        carouselView = findViewById(R.id.espacio_carousel);

        ImageButton imageButton = findViewById(R.id.espacio_capa_btn);
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

        // Inicializar mapa
        map = findViewById(R.id.espacio_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(ZOOM_MAPA);
        mapController.setCenter(calcularPuntoMedio());

        // Dibujar espacio
        map.getOverlayManager().add(espacioNatural.getPoligonoCoordenadas());

        obtenerImagenes();

        // Inicializar texto e imagen
        TextView espacioTipo = findViewById(R.id.espacio_tipo);
        TextView espacioFecha = findViewById(R.id.espacio_descripcion);
        ImageView espacioFoto = findViewById(R.id.espacio_foto);

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
            public void onClickItem(int position) {
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
    private GeoPoint calcularPuntoMedio() {
        double[] centroid = {0, 0};
        double[][] vertices = new double[2][espacioNatural.getCoordenadas().size()];
        for (int j = 0; j < espacioNatural.getCoordenadas().size(); j++) {
            vertices[0][j] = espacioNatural.getCoordenadas().get(j).getLatitude();
            vertices[1][j] = espacioNatural.getCoordenadas().get(j).getLongitude();
        }
        double signedArea = 0.0;
        double x0; // Current vertex X
        double y0; // Current vertex Y
        double x1; // Next vertex X
        double y1; // Next vertex Y
        double a;  // Partial signed area

        // For all vertices except last
        int i;
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
        BufferedReader bf = null;

        for (int i = 0; i < Utilidades.nombresFicheros.length; i++) {
            try {
                bf = new BufferedReader(new FileReader(getFilesDir() + "/" + Utilidades.nombresFicheros[i] + ".kml"));
                String linea;
                while ((linea = bf.readLine()) != null) {
                    if (linea.contains(espacioNatural.getCodigo())) {
                        set.add(i);
                        break;
                    }
                }
            } catch (Exception e) {
                Log.e("FICHERO", "Problema al abrir fichero", e);
            } finally {
                try {
                    Objects.requireNonNull(bf).close();
                } catch (IOException e) {
                    Log.e("FICHERO", "Problema al cerrar fichero", e);
                }
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
            Log.e("FLICKR", "Problema al descargar fotos", e);
        } catch (InterruptedException e) {
            Log.e("FLICKR", "Problema al descargar fotos", e);
            Thread.currentThread().interrupt();
        }
    }

    private String leerUrls(Context context) {
        String json;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.urls);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e("JSON", "Error al leer", e);
            return null;
        }
        return json;
    }

    class ObtenerImagenes extends AsyncTask<Void, Void, ArrayList<POI>> {
        @Override
        protected ArrayList<POI> doInBackground(Void... voids) {
            FlickrPOIProvider poiProvider = new FlickrPOIProvider(FLICKR_APIKEY);
            BoundingBox bb = BoundingBox.fromGeoPoints(espacioNatural.getCoordenadas());
            return poiProvider.getPOIInside(bb, 20);
        }
    }
}

class URLEspacio {
    private String codigo;
    private String urlJcyl;
    private String urlWikipedia;

    @NonNull
    @Override
    public String toString() {
        return "URLEspacio{" +
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
