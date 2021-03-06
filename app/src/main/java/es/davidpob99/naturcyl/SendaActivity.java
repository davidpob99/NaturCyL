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
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    static Senda senda;
    private MapView mapa;

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
                    mapa.getOverlays().clear();
                    mostrarDirecciones();
                    fab.setImageResource(R.drawable.ic_routes_white_24dp);
                    Toast.makeText(SendaActivity.this, "Direcciones activadas", Toast.LENGTH_SHORT).show();
                    direcciones = true;
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
                        // Continuar con la actividad
                    }
                })
                .create()
                .show();
        crearMapa();
        mostrarDirecciones();

        ImageButton imageButton = findViewById(R.id.senda_capa_btn);
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
        senda = null;
        mapa.onDetach();
    }

    private void crearMapa() {
        mapa = findViewById(R.id.senda_map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);
        IMapController mapController = mapa.getController();
        mapController.setZoom(ZOOM_MAPA);
        mapController.setCenter(senda.getCoordenadasSenda().get(0));
    }

    private void mostrarDirecciones() {
        ArrayList<GeoPoint> coordenadas = senda.getCoordenadasSenda();
        while (senda.getCoordenadasSenda().size() > 80) {
            for (int i = 1; i < senda.getCoordenadasSenda().size() - 1 && coordenadas.size() > 80; i += 2) {
                coordenadas.remove(i);
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
