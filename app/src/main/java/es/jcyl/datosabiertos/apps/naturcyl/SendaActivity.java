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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SendaActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 18;
    protected static Senda senda;
    private MapView mapa;
    private IMapController mapController;
    private ImageButton imageButton;

    private boolean direcciones = true;
    private boolean satelite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (direcciones) {
                    mapa.getOverlays().clear();
                    crearMapa();
                    Polyline ruta = new Polyline();
                    ruta.setPoints(senda.getCoordenadasSenda());
                    mapa.getOverlayManager().add(ruta);
                    direcciones = false;
                    fab.setImageResource(R.drawable.ic_directions_white_24dp);
                    Toast.makeText(SendaActivity.this, "Direcciones desactivadas", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO mapa.getOverlays().clear();
                    mostrarDirecciones();
                    fab.setImageResource(R.drawable.ic_routes_white_24dp);
                    Toast.makeText(SendaActivity.this, "Direcciones activadas", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Aviso
        new AlertDialog.Builder(this)
                .setTitle("Advertencia")
                .setMessage("La función de navegación está en desarrollo, por lo que puede ser poco precisa o incorrecta. Puede desactivarla pulsando el botón inferior.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
        crearMapa();
        mostrarDirecciones();

        imageButton = findViewById(R.id.senda_capa_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!satelite) {
                    MapBoxTileSource tileSource = new MapBoxTileSource("MapBoxSatelliteLabelled", 1, 19, 256, ".png");
                    //option 1, load your settings from the manifest
                    tileSource.setAccessToken(getResources().getString(R.string.MAPBOX_ACCESS_TOKEN));
                    tileSource.setMapboxMapid(getResources().getString(R.string.MAPBOX_MAPID));
                    mapa.setTileSource(tileSource);
                    satelite = true;
                } else {
                    mapa.setTileSource(TileSourceFactory.MAPNIK);
                    satelite = false;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapa.onDetach();
    }

    private void crearMapa() {
        mapa = findViewById(R.id.senda_map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);
        mapController = mapa.getController();
        mapController.setZoom(ZOOM_MAPA);
        mapController.setCenter(senda.getCoordenadasSenda().get(0));
    }

    private void mostrarDirecciones() {
        ArrayList<GeoPoint> coordenadas = senda.getCoordenadasSenda();
        while (senda.getCoordenadasSenda().size() > 80) {
            for (int i = 1; i < senda.getCoordenadasSenda().size() - 1 && coordenadas.size() > 80; i += 2) {
                coordenadas.remove(i);
                Log.i("POS", String.valueOf(coordenadas.size()));
            }
        }
        ObtenerSenda os = new ObtenerSenda();
        ObtenerSenda.GRAPHHOPPER_API_KEY = getString(R.string.GRAPHHOPPER_APIKEY);
        os.execute(coordenadas);
        Road road = null;
        try {
            road = os.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        mapa.getOverlays().add(roadOverlay);
        mapa.invalidate();

        Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_default);
        for (int i = 0; i < road.mNodes.size(); i++) {
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(mapa);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setTitle("Step " + i);
            nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
            mapa.getOverlays().add(nodeMarker);
        }
    }
}
