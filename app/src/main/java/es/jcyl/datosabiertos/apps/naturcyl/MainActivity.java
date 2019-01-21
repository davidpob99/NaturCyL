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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Pantalla principal de la aplicación. Inicializa y guarda todos los
 * datos a usar del portal de Datos Abiertos de la JCyL. Además muestra
 * en pantalla los espacios naturales disponibles.
 *
 * @author David Población Criado
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<EspacioNatural> listaEspacios;

    private RecyclerView rv;
    protected static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Diálogo de cargar
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Cargando datos, el tiempo depende de su conexión a Internet");

        //inicio
        inicializarEspacios();

        // Cargar RecyclerView
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Inicializa los espacios naturales, obteniéndo los datos de la web y guardándolos
     * en objetos de tipo EspacioNatural
     *
     * @see EspacioNatural
     */
    private void inicializarEspacios() {
        Document kmlEspacios = null;
        listaEspacios = new ArrayList<>();

        try {
            kmlEspacios = new ObtenerKml().execute(EspacioNatural.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

}
