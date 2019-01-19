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

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class ItemActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 17;
    private static final String MEDIDA_AREA = " m\u00B2";
    protected static ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> lista;

    private int posicion;
    private int tipo;

    private TextView nombre;
    private TextView codigo;
    private TextView area;
    private TextView estado;
    private TextView senalizacion;
    private TextView q;
    private TextView interesTuristico;
    private TextView acceso;
    private TextView observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapView map;
        IMapController mapController;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posicion = Integer.valueOf(getIntent().getStringExtra("posicion"));
        tipo = Integer.valueOf(getIntent().getStringExtra("tipo"));

        // Inicializar mapa
        map = findViewById(R.id.item_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(ZOOM_MAPA);
        mapController.setCenter(lista.getEstanEnEspacio().get(posicion).getCoordenadas());

        // Añadir marcador
        Marker marcador = new Marker(map);
        marcador.setPosition(lista.getEstanEnEspacio().get(posicion).getCoordenadas());
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marcador);

        // Añadir elementos comunes, de EspacioNaturalItem
        nombre = findViewById(R.id.nombre_item);
        codigo = findViewById(R.id.codigo_item);
        area = findViewById(R.id.area_item);
        estado = findViewById(R.id.estado_item);
        senalizacion = findViewById(R.id.senalizacion_item);
        q = findViewById(R.id.q_item);
        interesTuristico = findViewById(R.id.interes_turistico_item);
        acceso = findViewById(R.id.acceso_item);
        observaciones = findViewById(R.id.observaciones_item);

        inicializarComun();
        // No comun
        LinearLayout raiz = findViewById(R.id.particular_item);

        switch (tipo) {
            case 0:
                Aparcamiento a = (Aparcamiento) lista.get(posicion);
                TextView tv = new TextView(this);
                tv.setText("Delimitado");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText(booleanAEspanol(a.isDelimitado()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Aparca Bicis");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText(booleanAEspanol(a.isAparcaBicis()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;

        }
    }

    private void inicializarComun() {
        nombre.setText(lista.getEstanEnEspacio().get(posicion).getNombre());
        codigo.setText(lista.getEstanEnEspacio().get(posicion).getCodigo());
        area.setText(String.valueOf(lista.getEstanEnEspacio().get(posicion).getSuperficie()) + MEDIDA_AREA);
        estado.setText(EspacioNaturalItem.estados[lista.getEstanEnEspacio().get(posicion).getEstado() - 1]);
        senalizacion.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isSenalizacionExterna()));
        q.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isQ()));
        interesTuristico.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isInteresTuristico()));
        acceso.setText(lista.getEstanEnEspacio().get(posicion).getAcceso());
        observaciones.setText(lista.getEstanEnEspacio().get(posicion).getObservaciones());
    }

    private String booleanAEspanol(boolean b) {
        return b ? "Sí" : "No";
    }
}
