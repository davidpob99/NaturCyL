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

package es.jcyl.datosabiertos.apps.naturcyl;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class EspacioActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 12;
    private static final String URL_ORTOFOTO = "http://www.idecyl.jcyl.es/IGCyL/services/PaisajeCubierta/Ortofoto/MapServer/WMSServer?request=GetCapabilities&service=WMS";

    protected static EspacioNatural espacioNatural;
    private MapView map;
    private IMapController mapController;
    private int posicion;

    private TextView espacioTipo;
    private TextView espacioFecha;
    private ImageView espacioFoto;
    private FloatingActionButton fab;

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

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // Inicializar texto e imagen
        espacioTipo = findViewById(R.id.espacio_tipo);
        espacioFecha = findViewById(R.id.espacio_descripcion);
        espacioFoto = findViewById(R.id.espacio_foto);

        setTitle(espacioNatural.getNombre());
        espacioTipo.setText(espacioNatural.getTipoDeclaracion());
        espacioFecha.setText(espacioNatural.getFechaDeclaracion());
        Picasso.get().load(EspacioNatural.URL_IMG_BASE + espacioNatural.getImagen()).into(espacioFoto);

        // Cargar RecyclerView
        RecyclerView rv = findViewById(R.id.espacio_item_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapterEspacioItem adapter = new RVAdapterEspacioItem(Utilidades.crearItems(), new RVClickListenerEspacio() {
            @Override
            public void onClickItem(View v, int position) {
                ListaItemsActivity.espacioNatural = espacioNatural;
                Intent myIntent = new Intent(EspacioActivity.this, ListaItemsActivity.class);
                myIntent.putExtra("posicion", String.valueOf(position));
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
}
