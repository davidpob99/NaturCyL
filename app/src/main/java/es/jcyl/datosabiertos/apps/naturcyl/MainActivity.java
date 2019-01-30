/*
 * Aplicación para consultar los espacios naturales de la comunidad
 * autónoma de Castilla y León, así como sus equipamientos y
 * posibilidades.
 *
 * Copyright (C) 2018  David Población Criado
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dmax.dialog.SpotsDialog;

import static java.lang.System.exit;

/**
 * Pantalla principal de la aplicación. Inicializa y guarda todos los
 * datos a usar del portal de Datos Abiertos de la JCyL. Además muestra
 * en pantalla los espacios naturales disponibles.
 *
 * @author David Población Criado
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 112;
    private ArrayList<EspacioNatural> listaEspacios;

    private RecyclerView rv;
    protected static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencias = this.getSharedPreferences("es.davidpob99.naturcyl", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        // Primera ejecución
        /*if (preferencias.getBoolean("firstrun", true)) {
            editor.clear().commit();
            Gson gson = new Gson();
            // Favoritos favoritos = new Favoritos();
            String cadenaEspacio = gson.toJson(espa)
            Set<String> stringSet = new HashSet<>();
            editor.putStringSet("espacios_favoritos", espacioNaturalSet).commit();
            editor.putBoolean("firstrun", false).apply();
        }*/
        // startActivity(new Intent(MainActivity.this, IntroActivity.class));

        // Diálogo de cargar
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Cargando datos, el tiempo depende de su conexión a Internet");

        //inicio
        // Comprobar permisos
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // descargarTodo();
            inicializarEspacios(getApplicationContext().getFilesDir());
            Collections.sort(listaEspacios);
            cargarRV();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.accion_ajustes) {
            return true;
        } else if (id == R.id.accion_licencia_terceros) {
            Intent myIntent = new Intent(MainActivity.this, TextoActivity.class);
            myIntent.putExtra("accion", "licencia_tercero");
            startActivity(myIntent);
        } else if (id == R.id.accion_conjunto_datos) {
            Intent myIntent = new Intent(MainActivity.this, TextoActivity.class);
            myIntent.putExtra("accion", "conjunto_datos");
            startActivity(myIntent);
        } else if (id == R.id.accion_acerca_de) {
            Intent myIntent = new Intent(MainActivity.this, AcercaDeActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    descargarTodo();
                    inicializarEspacios(getApplicationContext().getFilesDir());
                    Collections.sort(listaEspacios);
                    cargarRV();
                } else {
                    Toast.makeText(this, "Sin el permiso de almacenamiento no puede funcionar la app. Saliendo...", Toast.LENGTH_LONG).show();
                    exit(1);
                }
            }
        }

    }

    /**
     * Inicializa los espacios naturales, obteniéndo los datos de la web y guardándolos
     * en objetos de tipo EspacioNatural
     *
     * @see EspacioNatural
     */
    private void inicializarEspacios(File dir) {
        Document kmlEspacios = null;
        listaEspacios = new ArrayList<>();
        File file = Utilidades.obtenerFichero(dir, "Espacios.kml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            kmlEspacios = db.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                kmlEspacios = new ObtenerKml().execute(EspacioNatural.URL_KML).get();
            } catch (ExecutionException r) {
                r.printStackTrace();
            } catch (InterruptedException r) {
                r.printStackTrace();
            }
        }
        NodeList nodosEspacios = kmlEspacios.getElementsByTagName("Placemark");
        for (int i = 0; i < nodosEspacios.getLength(); i++) {
            Node n = nodosEspacios.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                ArrayList<GeoPoint> lgp = new ArrayList<>();
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] t = c.split(" ");
                for (String s : t) {
                    String[] ll = s.replace(" ", "").split(",");
                    GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                    lgp.add(gp);
                }
                EspacioNatural en = new EspacioNatural(Integer.valueOf(nl.item(0).getTextContent()),
                        Boolean.valueOf(nl.item(3).getTextContent()),
                        nl.item(5).getTextContent(),
                        nl.item(6).getTextContent(),
                        nl.item(8).getTextContent().split("T")[0],
                        nl.item(11).getTextContent(),
                        lgp,
                        nl.item(9).getTextContent());
                listaEspacios.add(en);
            }
        }
        /*for (EspacioNatural en : listaEspacios) {
            Log.i("ESPACIO", en.toString());
        }*/
    }

    private void descargar(String nombreFichero, final String url) {
        PRDownloader.initialize(getApplicationContext());
        // Setting timeout globally for the download network requests:
        PRDownloaderConfig prDownloaderConfig = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), prDownloaderConfig);
        final AlertDialog ad = new SpotsDialog.Builder().setContext(this).setMessage("Descargando datos").build();
        ad.show();

        int downloadId = PRDownloader.download(url, getApplicationContext().getFilesDir().toString(), nombreFichero)
                .build()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        // Log.i("PROG", progress.toString());
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.i("INFO", "Archivo " + url + " descargado en" + getApplicationContext().getFilesDir().toString());
                        ad.dismiss();
                    }

                    @Override
                    public void onError(Error error) {
                        Log.e("ERROR", error.toString());
                        ad.dismiss();
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
                        } else {
                            builder = new AlertDialog.Builder(MainActivity.this);
                        }
                        builder.setTitle("Error")
                                .setMessage("Ha habido un error al descargar los datos. Revise su conexión a Internet")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                });
    }

    private void descargarTodo() {
        descargar("Espacios.kml", EspacioNatural.URL_KML);
        descargar("Aparcamientos.kml", Aparcamiento.URL_KML);
        descargar("Observatorios.kml", Observatorio.URL_KML);
        descargar("Miradores.kml", Mirador.URL_KML);
        descargar("ArbolesSingulares.kml", ArbolSingular.URL_KML);
        descargar("ZonasRecreativas.kml", ZonaRecreativa.URL_KML);
        descargar("CasasParque.kml", CasaParque.URL_KML);
        descargar("CentrosVisitante.kml", CentroVisitante.URL_KML);
        descargar("ZonasAcampada.kml", ZonaAcampada.URL_KML);
        descargar("Campamentos.kml", Campamento.URL_KML);
        descargar("Refugios.kml", Refugio.URL_KML);
        descargar("Quioscos.kml", Quiosco.URL_KML);
    }

    private void cargarRV() {
        RecyclerView rv = findViewById(R.id.espacio_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapterEspacio adapter = new RVAdapterEspacio(listaEspacios, new RVClickListenerEspacio() {
            @Override
            public void onClickItem(View v, int position) {
                EspacioActivity.espacioNatural = listaEspacios.get(position);
                Intent myIntent = new Intent(MainActivity.this, EspacioActivity.class);
                myIntent.putExtra("posicion", String.valueOf(position));
                startActivity(myIntent);
            }
        });
        rv.setAdapter(adapter);
    }
}
