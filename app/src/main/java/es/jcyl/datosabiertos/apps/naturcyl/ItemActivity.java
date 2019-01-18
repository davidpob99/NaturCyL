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
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class ItemActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 17;
    protected static EspacioNaturalItem espacioNaturalItem;

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

        // Inicializar mapa
        map = findViewById(R.id.item_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(ZOOM_MAPA);
        mapController.setCenter(espacioNaturalItem.getCoordenadas());

        // Añadir marcador
        Marker marcador = new Marker(map);
        marcador.setPosition(espacioNaturalItem.getCoordenadas());
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marcador);

        // Añadir elementos comunes, de EspacioNaturalItem
        LinearLayout raiz = findViewById(R.id.root_item);
        TextView tv = new TextView(this);
        tv.setText("Nombre");
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv.setPadding(8, 8, 8, 8);
        raiz.addView(tv);

        tv = new TextView(this);
        tv.setText(espacioNaturalItem.getNombre());
        tv.setPadding(8, 8, 8, 8);
        raiz.addView(tv);

        tv = new TextView(this);
        tv.setText("Acceso");
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv.setPadding(8, 8, 8, 8);
        raiz.addView(tv);

        tv = new TextView(this);
        tv.setText(espacioNaturalItem.getAcceso());
        tv.setPadding(8, 8, 8, 8);
        raiz.addView(tv);

        tv = new TextView(this);
        tv.setText("Observaciones");
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tv.setPadding(8, 8, 8, 8);
        raiz.addView(tv);

        tv = new TextView(this);
        tv.setText(espacioNaturalItem.getObservaciones());
        tv.setPadding(8, 8, 8, 8);
        raiz.addView(tv);
    }
}
