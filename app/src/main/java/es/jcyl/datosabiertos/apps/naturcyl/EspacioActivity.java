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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class EspacioActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final double ZOOM_MAPA = 12;
    private static final String URL_ORTOFOTO = "http://www.idecyl.jcyl.es/IGCyL/services/PaisajeCubierta/Ortofoto/MapServer/WMSServer?request=GetCapabilities&service=WMS";

    protected static EspacioNatural espacioNatural;
    private MapView map;
    private IMapController mapController;
    private int posicion;
    private Favoritos favoritos;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    private TextView espacioNombre;
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
                obtenerFavoritos();
                if (favoritosContieneEspacio()) {
                    Snackbar.make(view, "Eliminado de favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Eliminado", null).show();
                    favoritos.espacios.remove(espacioNatural);
                    cambiarImagen();
                } else {
                    Snackbar.make(view, "Guardado en favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Guardado", null).show();
                    favoritos.espacios.add(espacioNatural);
                    cambiarImagen();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*obtenerFavoritos();
        cambiarImagen();*/

        // Obtener de main
        String s = getIntent().getStringExtra("posicion");
        posicion = Integer.valueOf(s);

        // Comprobar permisos
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
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
        espacioNombre = findViewById(R.id.espacio_nombre);
        espacioTipo = findViewById(R.id.espacio_tipo);
        espacioFecha = findViewById(R.id.espacio_descripcion);
        espacioFoto = findViewById(R.id.espacio_foto);

        espacioNombre.setText(espacioNatural.getNombre());
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    /**
     * Dado el espacio natural que es un atributo de la clase calcula el punto medio
     * del polígono que forman sus coordenadas
     *
     * @return coordenadas del punto medio del polígono
     */
    public GeoPoint calcularPuntoMedio() {
        double x = 0.;
        double y = 0.;
        int cont = espacioNatural.getCoordenadas().size();
        for (int i = 0; i < cont - 1; i++) {
            final GeoPoint point = espacioNatural.getCoordenadas().get(i);
            x += point.getLatitude();
            y += point.getLongitude();
        }

        x = x / cont;
        y = y / cont;

        return new GeoPoint(x, y);
    }

    private boolean favoritosContieneEspacio() {
        return favoritos.espacios.contains(espacioNatural);
    }

    private void obtenerFavoritos() {
        preferencias = this.getSharedPreferences("es.davidpob99.naturcyl", Context.MODE_PRIVATE);
        editor = preferencias.edit();
        Gson gson = new Gson();
        String favsJson = preferencias.getString("favoritos", "");
        favoritos = gson.fromJson(favsJson, Favoritos.class);
    }

    private void guardarFavoritos() {
        preferencias = this.getSharedPreferences("es.davidpob99.naturcyl", Context.MODE_PRIVATE);
        editor = preferencias.edit();
        Gson gson = new Gson();
        String favsJson = gson.toJson(favoritos);
        editor.putString("favoritos", favsJson).commit();
    }

    private void cambiarImagen() {
        if (favoritosContieneEspacio()) {
            fab.setImageResource(R.drawable.ic_star_white_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_star_border_white_24dp);
        }
    }
}
