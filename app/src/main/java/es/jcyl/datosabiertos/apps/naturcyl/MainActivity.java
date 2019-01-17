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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private ArrayList<Aparcamiento> listaAparcamientos;
    private ArrayList<Observatorio> listaObservatorios;
    private ArrayList<Mirador> listaMiradores;
    private ArrayList<ZonaRecreativa> listaZonasRecreativas;
    private ArrayList<CasaParque> listaCasasParque;
    private ArrayList<CentroVisitante> listaCentrosVisitantes;
    private ArrayList<ArbolSingular> listaArbolesSingulares;

    private RecyclerView rv;
    protected static final String[] items = {"Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"};

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

        //inicio
        inicializarEspacios();
        /*
        inicializarAparcamientos();
        inicializarObservatorios();
        inicializarMiradores();
        inicializarZonasRecreativas();
        inicializarCasasParque();
        inicializarArbolesSingulares();
        inicializarCentrosVisitantes();*/

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
        for (EspacioNatural en : listaEspacios) {
            Log.i("ESPACIO", en.toString());
        }
    }

    /**
     * Inicializa los aparcamientos, obteniéndo los datos de la web y guardándolos
     * en objetos de tipo Aparcamiento
     *
     * @see Aparcamiento
     */
    private void inicializarAparcamientos() {
        Document kmlAparcamientos = null;
        listaAparcamientos = new ArrayList<>();

        try {
            kmlAparcamientos = new ObtenerKml().execute(Aparcamiento.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodosAparcamientos = kmlAparcamientos.getElementsByTagName("Placemark");
        for (int i = 0; i < nodosAparcamientos.getLength(); i++) {
            Node n = nodosAparcamientos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                Aparcamiento a = new Aparcamiento();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                a.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                a.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                a.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                a.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                a.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                a.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                a.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                a.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                a.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                a.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                a.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                a.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "aparcamiento_delimitacion":
                                a.setDelimitado(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "aparcamiento_aparcabicicletas":
                                a.setAparcaBicis(Boolean.valueOf(no.getTextContent()));
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                a.setCoordenadas(gp);
                /*
                Aparcamiento a = new Aparcamiento(Integer.valueOf(nl.item(0).getTextContent()),
                        nl.item(5).getTextContent(),
                        nl.item(8).getTextContent().split("T")[0],
                        Integer.valueOf(nl.item(9).getTextContent()),
                        nl.item(7).getTextContent().split("T")[0],
                        Boolean.valueOf(nl.item(10).getTextContent()),
                        nl.item(6).getTextContent(),
                        nl.item(11).getTextContent(),
                        Boolean.valueOf(nl.item(16).getTextContent()),
                        Double.valueOf(nl.item(17).getTextContent()),
                        Boolean.valueOf(nl.item(20).getTextContent()),
                        Boolean.valueOf(nl.item(21).getTextContent()),
                        gp);*/
                listaAparcamientos.add(a);
            }
        }
    }

    /**
     * Inicializa los observatorios, obteniéndo los datos de la web y guardándolos
     * en objetos de tipo Observatorio
     *
     * @see Observatorio
     */
    public void inicializarObservatorios() {
        Document kml = null;
        listaObservatorios = new ArrayList<>();

        try {
            kml = new ObtenerKml().execute(Observatorio.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodos = kml.getElementsByTagName("Placemark");
        for (int i = 0; i < nodos.getLength(); i++) {
            Node n = nodos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                Observatorio o = new Observatorio();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                o.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                o.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                o.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                o.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                o.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                o.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                o.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                o.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                o.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                o.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                o.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                o.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "observatorio_tipo":
                                o.setTipoObservatorio(Integer.valueOf(no.getTextContent()));
                                break;
                            case "observatorio_entorno":
                                o.setEntornoObservatorio(no.getTextContent());
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                o.setCoordenadas(gp);
                listaObservatorios.add(o);
            }
        }
    }

    /**
     * Inicializa los miradores, obteniéndo los datos de la web y guardándolos
     * en objetos de tipo Mirador
     *
     * @see Mirador
     */
    public void inicializarMiradores() {
        Document kml = null;
        listaMiradores = new ArrayList<>();

        try {
            kml = new ObtenerKml().execute(Mirador.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodos = kml.getElementsByTagName("Placemark");
        for (int i = 0; i < nodos.getLength(); i++) {
            Node n = nodos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                Mirador m = new Mirador();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                m.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                m.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                m.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                m.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                m.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                m.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                m.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                m.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                m.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                m.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                m.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                m.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "mirador_entorno_ambiental":
                                m.setEntorno(no.getTextContent());
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                m.setCoordenadas(gp);
                listaMiradores.add(m);
            }
        }
    }

    private void inicializarZonasRecreativas() {
        Document kml = null;
        listaZonasRecreativas = new ArrayList<>();

        try {
            kml = new ObtenerKml().execute(ZonaRecreativa.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodos = kml.getElementsByTagName("Placemark");
        for (int i = 0; i < nodos.getLength(); i++) {
            Node n = nodos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                ZonaRecreativa zr = new ZonaRecreativa();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                zr.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                zr.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                zr.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                zr.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                zr.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                zr.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                zr.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                zr.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                zr.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                zr.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                zr.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                zr.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "zona_rec_es_merendero":
                                zr.setMerendero(Boolean.valueOf(no.getTextContent()));
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                zr.setCoordenadas(gp);
                listaZonasRecreativas.add(zr);
            }
        }
    }

    public void inicializarCasasParque() {
        Document kml = null;
        listaCasasParque = new ArrayList<>();

        try {
            kml = new ObtenerKml().execute(ZonaRecreativa.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodos = kml.getElementsByTagName("Placemark");
        for (int i = 0; i < nodos.getLength(); i++) {
            Node n = nodos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                CasaParque cp = new CasaParque();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                cp.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                cp.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                cp.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                cp.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                cp.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                cp.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                cp.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                cp.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                cp.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                cp.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                cp.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                cp.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "casa_parque_servicio_informativo":
                                cp.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "casa_parque_cida_biblio":
                                cp.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "casa_parque_tienda_verde":
                                cp.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                cp.setCoordenadas(gp);
                listaCasasParque.add(cp);
            }
        }
    }

    public void inicializarCentrosVisitantes() {
        Document kml = null;
        listaCentrosVisitantes = new ArrayList<>();

        try {
            kml = new ObtenerKml().execute(ZonaRecreativa.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodos = kml.getElementsByTagName("Placemark");
        for (int i = 0; i < nodos.getLength(); i++) {
            Node n = nodos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                CentroVisitante cv = new CentroVisitante();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                cv.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                cv.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                cv.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                cv.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                cv.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                cv.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                cv.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                cv.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                cv.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                cv.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                cv.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                cv.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "otro_punto_interes_tipo":
                                cv.setTipo(Integer.valueOf(no.getTextContent()));
                                break;
                            case "otro_punto_interes_descripcion":
                                cv.setDescripcion(no.getTextContent());
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                cv.setCoordenadas(gp);
                listaCentrosVisitantes.add(cv);
            }
        }
    }

    public void inicializarArbolesSingulares() {
        Document kml = null;
        listaArbolesSingulares = new ArrayList<>();

        try {
            kml = new ObtenerKml().execute(ZonaRecreativa.URL_KML).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NodeList nodos = kml.getElementsByTagName("Placemark");
        for (int i = 0; i < nodos.getLength(); i++) {
            Node n = nodos.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                NodeList nl = e.getElementsByTagName("SimpleData");
                ArbolSingular as = new ArbolSingular();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "atr_gr_id":
                                as.setId(Integer.valueOf(no.getTextContent()));
                                break;
                            case "atr_gr_tiene_q":
                                as.setQ(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_a_codigo":
                                as.setCodigo(no.getTextContent());
                                break;
                            case "equip_a_observaciones":
                                as.setObservaciones(no.getTextContent());
                                break;
                            case "equip_a_estado_fecha":
                                as.setFechaEstado(no.getTextContent().split("T")[0]);
                                break;
                            case "equip_a_fecha_declaracion":
                                as.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                                break;
                            case "estado_id":
                                as.setEstado(Integer.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_senalizacion_ext":
                                as.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_acceso_modo":
                                as.setAcceso(no.getTextContent());
                                break;
                            case "equip_b_nombre":
                                as.setNombre(no.getTextContent());
                                break;
                            case "equip_b_tiene_interes":
                                as.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "equip_b_superficie_aprox":
                                as.setSuperficie(Double.valueOf(no.getTextContent()));
                                break;
                            case "arbol_nombre":
                                as.setNombreArbol(no.getTextContent());
                                break;
                            case "especie_id":
                                as.setEspecie(Integer.valueOf(no.getTextContent()));
                                break;
                        }
                    }
                }
                String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] ll = c.split(",");
                GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
                as.setCoordenadas(gp);
                listaArbolesSingulares.add(as);
            }
        }
    }
}
