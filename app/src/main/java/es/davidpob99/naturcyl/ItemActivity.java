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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;


public class ItemActivity extends AppCompatActivity {
    private static final double ZOOM_MAPA = 17;
    private static final String MEDIDA_AREA = " m\u00B2";
    protected static ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> lista;

    private int posicion;
    private int tipo;
    private boolean satelite = false;

    private TextView codigo;
    private TextView area;
    private TextView estado;
    private TextView senalizacion;
    private TextView q;
    private TextView interesTuristico;
    private TextView acceso;
    private TextView observaciones;
    private FloatingActionButton fab;
    private ImageButton imageButton;
    private MapView map;

    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    private ArrayList<EspacioNaturalItem> favoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IMapController mapController;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipo == 11) {
                    SendaActivity.senda = (Senda) lista.getEstanEnEspacio().get(posicion);
                    Intent myIntent = new Intent(ItemActivity.this, SendaActivity.class);
                    startActivity(myIntent);

                } else {
                    if (favoritosContieneEspacio()) {
                        Snackbar.make(view, "Eliminado de favoritos", Snackbar.LENGTH_LONG)
                                .setAction("Eliminado", null).show();
                        for (EspacioNaturalItem eni : favoritos) {
                            if (eni.getNombre().equals(lista.getEstanEnEspacio().get(posicion).getNombre())) {
                                favoritos.remove(eni);
                            }
                        }
                        cambiarImagen();
                        guardarFavoritos();
                    } else {
                        Snackbar.make(view, "Guardado en favoritos", Snackbar.LENGTH_LONG)
                                .setAction("Guardado", null).show();
                        favoritos.add(lista.getEstanEnEspacio().get(posicion));
                        cambiarImagen();
                        guardarFavoritos();
                    }
                }
            }
        });

        imageButton = findViewById(R.id.item_capa_btn);
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
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        obtenerFavoritos();
        cambiarImagen();

        posicion = Integer.valueOf(getIntent().getStringExtra("posicion"));
        tipo = Integer.valueOf(getIntent().getStringExtra("tipo"));
        setTitle(lista.getEstanEnEspacio().get(posicion).getNombre());

        // Inicializar mapa
        map = findViewById(R.id.item_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(ZOOM_MAPA);
        // Comprobar si es senda
        if (tipo == 11) {
            fab.setImageResource(R.drawable.ic_directions_white_24dp);
            Senda senda = (Senda) lista.getEstanEnEspacio().get(posicion);
            Polyline line = new Polyline();   //see note below!
            line.setPoints(senda.getCoordenadasSenda());
            map.getOverlayManager().add(line);
            mapController.setCenter(senda.getCoordenadasSenda().get(Math.round(senda.getCoordenadasSenda().size() / 2)));

        } else {
            mapController.setCenter(lista.getEstanEnEspacio().get(posicion).getCoordenadas());
            // Añadir marcador
            Resources res = getResources();
            Marker marcador = new Marker(map);
            if (tipo != -1) {
                Drawable drawable = res.getDrawable(Utilidades.fotosItems[tipo]);
                marcador.setIcon(drawable);
            }
            marcador.setPosition(lista.getEstanEnEspacio().get(posicion).getCoordenadas());
            marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marcador.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return false;
                }
            });
            map.getOverlays().add(marcador);
        }

        // Añadir elementos comunes, de EspacioNaturalItem
        codigo = findViewById(R.id.codigo_item);
        area = findViewById(R.id.area_item);
        estado = findViewById(R.id.estado_item);
        senalizacion = findViewById(R.id.senalizacion_item);
        q = findViewById(R.id.q_item);
        interesTuristico = findViewById(R.id.interes_turistico_item);
        acceso = findViewById(R.id.acceso_item);
        observaciones = findViewById(R.id.observaciones_item);

        inicializarComun();
        inicializarConcreto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        if (tipo == 11) {
            menu.removeItem(R.id.accion_streetview);
            menu.removeItem(R.id.accion_abrir);
            menu.removeItem(R.id.accion_como_llegar);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accion_compartir) {
            String compartirComun = "Visite "
                    + lista.getEstanEnEspacio().get(posicion).getNombre()
                    + " en el espacio de " + lista.getEspacioNatural().getNombre();
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            if (tipo == 11) {
                i.putExtra(android.content.Intent.EXTRA_TEXT, compartirComun);
            } else {
                i.putExtra(android.content.Intent.EXTRA_TEXT, compartirComun
                        + ". http://maps.google.com/maps?daddr="
                        + lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLatitude()
                        + ","
                        + lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLongitude());
            }
            startActivity(Intent.createChooser(i, "Compartir"));
        } else if (id == R.id.accion_abrir) {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
                    lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLatitude(),
                    lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } else if (id == R.id.accion_streetview) {
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll="
                    + lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLatitude()
                    + ","
                    + lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLongitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } else if (id == R.id.accion_como_llegar) {
            String uri = "http://maps.google.com/maps?daddr="
                    + lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLatitude()
                    + "," + lista.getEstanEnEspacio().get(posicion).getCoordenadas().getLongitude()
                    + " (" + lista.getEstanEnEspacio().get(posicion).getNombre() + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarComun() {
        codigo.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isAccesibilidad()));
        area.setText(lista.getEstanEnEspacio().get(posicion).getSuperficie() != 0 ? String.valueOf(lista.getEstanEnEspacio().get(posicion).getSuperficie()) + MEDIDA_AREA : Utilidades.NO_DATO);
        try {
            estado.setText(EspacioNaturalItem.estados[lista.getEstanEnEspacio().get(posicion).getEstado() - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            estado.setText("Estado: Sin determinar");
        }
        senalizacion.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isSenalizacionExterna()));
        q.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isQ()));
        interesTuristico.setText(booleanAEspanol(lista.getEstanEnEspacio().get(posicion).isInteresTuristico()));
        acceso.setText(lista.getEstanEnEspacio().get(posicion).getAcceso() != "" ? lista.getEstanEnEspacio().get(posicion).getAcceso() : Utilidades.NO_DATO);
        observaciones.setText(lista.getEstanEnEspacio().get(posicion).getObservaciones() != "" ? lista.getEstanEnEspacio().get(posicion).getObservaciones() : Utilidades.NO_DATO);
    }

    private String booleanAEspanol(boolean b) {
        return b ? "Sí" : "No";
    }

    private void inicializarConcreto() {
        LinearLayout raiz = findViewById(R.id.particular_item);
        TextView tv;
        switch (tipo) {
            case 0:
                Aparcamiento a = (Aparcamiento) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Delimitado");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(a.isDelimitado()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Aparca Bicis");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(a.isAparcaBicis()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 1:
                Observatorio o = (Observatorio) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Tipo");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(Observatorio.TIPOS[o.getTipoObservatorio() - 1 < 3 ? o.getTipoObservatorio() : 2]);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Entorno");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(o.getEntornoObservatorio());
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 2:
                Mirador m = (Mirador) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Entorno");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(m.getEntorno());
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 3:
                ZonaRecreativa zr = (ZonaRecreativa) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Merendero");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(zr.isMerendero()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 4:
                CasaParque cp = (CasaParque) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Servicio informativo");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(cp.isServicioInformativo()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Biblioteca");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(cp.isBiblioteca()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Tienda verde");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(cp.isTiendaVerde()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Página web");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(cp.getWeb());
                Linkify.addLinks(tv, Linkify.WEB_URLS);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 5:
                CentroVisitante cv = (CentroVisitante) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Tipo");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(CentroVisitante.TIPOS[cv.getTipo() - 1 < 10 ? cv.getTipo() : 9]); //TODO OUTOFBOUNDS
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Descripción");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(cv.getDescripcion());
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 6:
                ArbolSingular as = (ArbolSingular) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Nombre");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(as.getNombreArbol());
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 7:
                break;
            case 8:
                Campamento c = (Campamento) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Número de cabañas");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(String.valueOf(c.getCabanas()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Número de parcelas");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(String.valueOf(c.getParcelas()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Tipo de camping");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(Campamento.TIPOS[c.getTipoCamping() - 1 < 3 ? c.getTipoCamping() - 1 : 3]);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Página web");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(c.getWeb());
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 9:
                Refugio r = (Refugio) lista.getEstanEnEspacio().get(posicion);
                tv = new TextView(this);
                tv.setText("Tipo");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(Refugio.TIPOS[r.getTipo() - 1 < 5 ? r.getTipo() : 4]);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Uso");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(Refugio.USOS[r.getUso() - 1 < 3 ? r.getUso() : 2]);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Actividad");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(r.getActividad());
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Capacidad de pernoctación");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(r.isCapacidadPernoctacion()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setText("Servicio de comida");
                tv.setTypeface(null, Typeface.BOLD);
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                tv = new TextView(this);
                tv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                tv.setText(booleanAEspanol(r.isServicioComida()));
                tv.setPadding(8, 8, 8, 8);
                raiz.addView(tv);
                break;
            case 11:
                Senda s = (Senda) lista.getEstanEnEspacio().get(posicion);
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View tablaSenda = layoutInflater.inflate(R.layout.table_senda, null);
                raiz.addView(tablaSenda);

                TextView longitud = tablaSenda.findViewById(R.id.longitud_senda);
                TextView tiempo = tablaSenda.findViewById(R.id.tiempo_senda);
                TextView desnivel = tablaSenda.findViewById(R.id.desnivel_senda);
                TextView dificultad = tablaSenda.findViewById(R.id.dificultad_senda);
                TextView ciclabilidad = tablaSenda.findViewById(R.id.ciclabilidad_senda);
                TextView codigo = tablaSenda.findViewById(R.id.codigo_senda);
                ImageView fotoDificultad = tablaSenda.findViewById(R.id.foto_dificultad_senda);

                Log.i("SENDA", s.toString());

                longitud.setText(s.getLongitud() + " m");
                tiempo.setText(s.getTiempoRecorrido() + " min");
                desnivel.setText(s.getDesnivel() + " m");
                try {
                    dificultad.setText(Senda.DIFICULTAD[s.getDificultad() - 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    dificultad.setText(Utilidades.NO_DATO);
                }

                ciclabilidad.setText(s.getCiclabilidad() + " %");
                codigo.setText(s.getCodigoSenda());
                try {
                    fotoDificultad.setColorFilter(getResources().getColor(Senda.COLORES_DIFICULTAD[s.getDificultad() - 1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    fotoDificultad.setColorFilter(Senda.COLORES_DIFICULTAD[1]);
                }

            case -1:
                break;
        }
    }

    private boolean favoritosContieneEspacio() {
        for (EspacioNaturalItem eni : favoritos) {
            if (eni.getNombre().equals(lista.getEstanEnEspacio().get(posicion).getNombre())) {
                return true;
            }
        }
        return false;
    }

    private void obtenerFavoritos() {
        preferencias = getSharedPreferences("es.davidpob99.naturcyl", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferencias.getString("favoritos", null);
        Type type = new TypeToken<ArrayList<EspacioNaturalItem>>() {
        }.getType();
        favoritos = gson.fromJson(json, type);
        if (favoritos == null) {
            favoritos = new ArrayList<>();
        }
    }

    private void guardarFavoritos() {
        preferencias = this.getSharedPreferences("es.davidpob99.naturcyl", Context.MODE_PRIVATE);
        editor = preferencias.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoritos);
        editor.putString("favoritos", json);
        editor.apply();
    }

    private void cambiarImagen() {
        if (favoritosContieneEspacio()) {
            fab.setImageResource(R.drawable.ic_star_white_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_star_border_white_24dp);
        }
    }
}
