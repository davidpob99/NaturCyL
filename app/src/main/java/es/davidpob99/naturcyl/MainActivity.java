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

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
    private ProgressDialog pDialog;

    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    private ArrayList<EspacioNaturalItem> favoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferencias = getSharedPreferences("es.davidpob99.naturcyl", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFavoritos();
                FavoritosActivity.favoritos = favoritos;
                Intent myIntent = new Intent(MainActivity.this, FavoritosActivity.class);
                startActivity(myIntent);
            }
        });

        // Primera ejecución
        if (preferencias.getBoolean("firstrun", true)) {
            editor.clear().commit();
            editor.putString("favoritos", "").apply();
            editor.putBoolean("firstrun", false).commit();
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Al usar esta app, aceptas la Política de Privacidad y la Licencia (GNU GPL v3). Puedes consultar ambas en cualquier momento en el apartado 'Acerca de la app' ")
                    .setNeutralButton(getString(R.string.politica_privacidad), new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent myIntent = new Intent(MainActivity.this, TextoActivity.class);
                            myIntent.putExtra("accion", "politica_privacidad");
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton(getString(R.string.gnu_gpl_v3), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0-standalone.html"));
                            startActivity(browserIntent);
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

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
        if (id == R.id.accion_actualizar_datos) {
            ProgressDialog progress = ProgressDialog.show(this, "dialog title",
                    "dialog message", true);
            progress.show();
            descargarTodo();
            inicializarEspacios(getApplicationContext().getFilesDir());
            Collections.sort(listaEspacios);
            cargarRV();
            progress.dismiss();
        } else if (id == R.id.accion_conjunto_datos) {
            Intent myIntent = new Intent(MainActivity.this, TextoActivity.class);
            myIntent.putExtra("accion", "conjunto_datos");
            startActivity(myIntent);
        } else if (id == R.id.accion_acerca_de) {
            Intent myIntent = new Intent(MainActivity.this, AcercaDeActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.accion_cercanos) {
            Intent myIntent = new Intent(MainActivity.this, CercanosActivity.class);
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
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            exit(1);
                        }
                    }, 4000);
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

    private void descargarTodo() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            ParametrosDescarga[] descargas = {
                    new ParametrosDescarga(EspacioNatural.URL_KML, "Espacios.kml"),
                    new ParametrosDescarga(Aparcamiento.URL_KML, "Aparcamientos.kml"),
                    new ParametrosDescarga(Observatorio.URL_KML, "Observatorios.kml"),
                    new ParametrosDescarga(Mirador.URL_KML, "Miradores.kml"),
                    new ParametrosDescarga(ArbolSingular.URL_KML, "ArbolesSingulares.kml"),
                    new ParametrosDescarga(ZonaRecreativa.URL_KML, "ZonasRecreativas.kml"),
                    new ParametrosDescarga(CasaParque.URL_KML, "CasasParque.kml"),
                    new ParametrosDescarga(CentroVisitante.URL_KML, "CentrosVisitante.kml"),
                    new ParametrosDescarga(ZonaAcampada.URL_KML, "ZonasAcampada.kml"),
                    new ParametrosDescarga(Campamento.URL_KML, "Campamentos.kml"),
                    new ParametrosDescarga(Refugio.URL_KML, "Refugios.kml"),
                    new ParametrosDescarga(Quiosco.URL_KML, "Quioscos.kml"),
                    new ParametrosDescarga(Senda.URL_KML, "Sendas.kml")

            };
            new DescargarKMLs().execute(descargas);

            /*
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
            descargar("Sendas.kml", Senda.URL_KML);*/
        } else {
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

    class DescargarKMLs extends AsyncTask<ParametrosDescarga, String, String> {

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //System.out.println("Starting download");

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Descargando datos, por favor espere");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(ParametrosDescarga... f_url) {
            for (ParametrosDescarga pd : f_url) {
                int count;
                try {
                    //System.out.println("Downloading");
                    URL url = new URL(pd.url);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();
                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(getFilesDir() + "/" + pd.nombreFichero);
                    byte data[] = new byte[1024];

                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        // writing data to file
                        output.write(data, 0, count);

                    }
                    // flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
            return null;
        }

        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            //System.out.println("Downloaded");

            pDialog.dismiss();
        }

    }

    class ParametrosDescarga {
        String url;
        String nombreFichero;

        public ParametrosDescarga(String url, String nombreFichero) {
            this.url = url;
            this.nombreFichero = nombreFichero;
        }
    }
}
