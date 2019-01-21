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

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.GeoPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Utilidades {
    public static final String[] nombresItems = {
            "Aparcamientos",
            "Observatorios",
            "Miradores",
            "Zonas recreativas",
            "Casas del parque",
            "Centros de visitantes",
            "Árboles singulares",
            "Zonas de acampada",
            "Campamentos",
            "Refugios"

    };
    private static final int[] fotosItems = {
            R.drawable.ic_local_parking_black_24dp,
            R.drawable.ic_binoculars_black_24dp,
            R.drawable.ic_remove_red_eye_black_24dp,
            R.drawable.ic_beach_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_bank_black_24dp,
            R.drawable.ic_tree_black_24dp,
            R.drawable.ic_tent_black_24dp,
            R.drawable.ic_tent_black_24dp,
            R.drawable.ic_castle_black_24dp
    };

    public static KmlDocument obtenerKmlDeUrl(String url) {
        KmlDocument kml = new KmlDocument();
        kml.parseKMLUrl(url);
        return kml;
    }

    public static ArrayList<EItem> crearItems() {
        ArrayList<EItem> items = new ArrayList<>();
        for (int i = 0; i < nombresItems.length; i++) {
            EItem item = new EItem(nombresItems[i], fotosItems[i]);
            items.add(item);
        }
        return items;
    }

    public static void inicializarComun(Element el, Node no, EspacioNaturalItem eni) {
        switch (el.getAttribute("name")) {
            case "atr_gr_id":
                eni.setId(Integer.valueOf(no.getTextContent()));
                break;
            case "atr_gr_tiene_q":
                eni.setQ(Boolean.valueOf(no.getTextContent()));
                break;
            case "equip_a_codigo":
                eni.setCodigo(no.getTextContent());
                break;
            case "equip_a_observaciones":
                eni.setObservaciones(no.getTextContent());
                break;
            case "equip_a_estado_fecha":
                eni.setFechaEstado(no.getTextContent().split("T")[0]);
                break;
            case "equip_a_fecha_declaracion":
                eni.setFechaDeclaracion(no.getTextContent().split("T")[0]);
                break;
            case "estado_id":
                eni.setEstado(Integer.valueOf(no.getTextContent()));
                break;
            case "equip_b_senalizacion_ext":
                eni.setSenalizacionExterna(Boolean.valueOf(no.getTextContent()));
                break;
            case "equip_b_acceso_modo":
                eni.setAcceso(no.getTextContent());
                break;
            case "equip_b_nombre":
                eni.setNombre(no.getTextContent());
                break;
            case "equip_b_tiene_interes":
                eni.setInteresTuristico(Boolean.valueOf(no.getTextContent()));
                break;
            case "equip_b_superficie_aprox":
                eni.setSuperficie(Double.valueOf(no.getTextContent()));
                break;
        }
    }

    public static void inicializarCoordenadasComun(Element e, EspacioNaturalItem eni) {
        String c = e.getElementsByTagName("coordinates").item(0).getTextContent();
        String[] ll = c.split(",");
        GeoPoint gp = new GeoPoint(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
        eni.setCoordenadas(gp);
    }

    protected static ListaEspaciosNaturalesItems<Aparcamiento> inicializarAparcamientos() {
        Document kml = null;
        ListaEspaciosNaturalesItems<Aparcamiento> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(Aparcamiento.URL_KML).get();
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
                Aparcamiento a = new Aparcamiento();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "aparcamiento_delimitacion":
                                a.setDelimitado(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "aparcamiento_aparcabicicletas":
                                a.setAparcaBicis(Boolean.valueOf(no.getTextContent()));
                                break;
                            default:
                                inicializarComun(el, no, a);
                                break;
                        }
                    }
                }
                inicializarCoordenadasComun(e, a);
                lista.add(a);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<Observatorio> inicializarObservatorios() {
        Document kml = null;
        ListaEspaciosNaturalesItems<Observatorio> lista = new ListaEspaciosNaturalesItems<>();

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
                            case "observatorio_tipo":
                                o.setTipoObservatorio(Integer.valueOf(no.getTextContent()));
                                break;
                            case "observatorio_entorno":
                                o.setEntornoObservatorio(no.getTextContent());
                                break;
                            default:
                                inicializarComun(el, no, o);
                                break;
                        }
                    }
                }
                inicializarCoordenadasComun(e, o);
                lista.add(o);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<Mirador> inicializarMiradores() {
        Document kml = null;
        ListaEspaciosNaturalesItems<Mirador> lista = new ListaEspaciosNaturalesItems<>();

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
                            case "mirador_entorno_ambiental":
                                m.setEntorno(no.getTextContent());
                                break;
                            default:
                                inicializarComun(el, no, m);
                        }
                    }
                }
                inicializarCoordenadasComun(e, m);
                lista.add(m);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<ZonaRecreativa> inicializarZonasRecreativas() {
        Document kml = null;
        ListaEspaciosNaturalesItems<ZonaRecreativa> lista = new ListaEspaciosNaturalesItems<>();

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
                            case "zona_rec_es_merendero":
                                zr.setMerendero(Boolean.valueOf(no.getTextContent()));
                                break;
                            default:
                                inicializarComun(el, no, zr);
                                break;
                        }
                    }
                }
                inicializarCoordenadasComun(e, zr);
                lista.add(zr);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<CasaParque> inicializarCasasParque() {
        Document kml = null;
        ListaEspaciosNaturalesItems<CasaParque> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(CasaParque.URL_KML).get();
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
                            case "casa_parque_servicio_informativo":
                                cp.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "casa_parque_cida_biblio":
                                cp.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "casa_parque_tienda_verde":
                                cp.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                            default:
                                inicializarComun(el, no, cp);
                        }
                    }
                }
                inicializarCoordenadasComun(e, cp);
                lista.add(cp);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<CentroVisitante> inicializarCentrosVisitantes() {
        Document kml = null;
        ListaEspaciosNaturalesItems<CentroVisitante> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(CentroVisitante.URL_KML).get();
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
                            case "otro_punto_interes_tipo":
                                cv.setTipo(Integer.valueOf(no.getTextContent()));
                                break;
                            case "otro_punto_interes_descripcion":
                                cv.setDescripcion(no.getTextContent());
                                break;
                            default:
                                inicializarComun(el, no, cv);
                                break;
                        }
                    }
                }
                inicializarCoordenadasComun(e, cv);
                lista.add(cv);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<ArbolSingular> inicializarArbolesSingulares() {
        Document kml = null;
        ListaEspaciosNaturalesItems<ArbolSingular> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(ArbolSingular.URL_KML).get();
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
                            case "arbol_nombre":
                                as.setNombreArbol(no.getTextContent());
                                break;
                            case "especie_id":
                                as.setEspecie(Integer.valueOf(no.getTextContent()));
                                break;
                            default:
                                inicializarComun(el, no, as);
                        }
                    }
                }
                inicializarCoordenadasComun(e, as);
                lista.add(as);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<ZonaAcampada> inicializarZonasAcampada() {
        Document kml = null;
        ListaEspaciosNaturalesItems<ZonaAcampada> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(ZonaAcampada.URL_KML).get();
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
                ZonaAcampada za = new ZonaAcampada();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            default:
                                inicializarComun(el, no, za);
                        }
                    }
                }
                inicializarCoordenadasComun(e, za);
                lista.add(za);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<Campamento> inicializarCampamentos() {
        Document kml = null;
        ListaEspaciosNaturalesItems<Campamento> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(Campamento.URL_KML).get();
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
                Campamento c = new Campamento();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "campamento_servicio_informativo":
                                c.setServicioInformativo(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "campamento_cabanas":
                                c.setCabanas(Integer.valueOf(no.getTextContent()));
                                break;
                            case "campamento_parcelas":
                                c.setParcelas(Integer.valueOf(no.getTextContent()));
                                break;
                            case "campamento_categoria":
                                c.setTipoCamping(Integer.valueOf(no.getTextContent()));
                                break;
                            case "web":
                                c.setWeb(no.getTextContent());
                                break;
                            default:
                                inicializarComun(el, no, c);
                                break;
                        }
                    }
                }
                inicializarCoordenadasComun(e, c);
                lista.add(c);
            }
        }
        return lista;
    }

    public static ListaEspaciosNaturalesItems<Refugio> inicializarRefugios() {
        Document kml = null;
        ListaEspaciosNaturalesItems<Refugio> lista = new ListaEspaciosNaturalesItems<>();

        try {
            kml = new ObtenerKml().execute(Refugio.URL_KML).get();
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
                Refugio r = new Refugio();

                for (int j = 0; j < nl.getLength(); j++) {
                    Node no = nl.item(j);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) no;
                        switch (el.getAttribute("name")) {
                            case "refugio_tipo":
                                r.setTipo(Integer.valueOf(no.getTextContent()));
                                break;
                            case "refugio_uso":
                                r.setUso(Integer.valueOf(no.getTextContent()));
                                break;
                            case "refugio_actividad":
                                r.setActividad(no.getTextContent());
                                break;
                            case "refugio_capacidad_pernoctar":
                                r.setCapacidadPernoctacion(Boolean.valueOf(no.getTextContent()));
                                break;
                            case "refugio_servicio_comida":
                                r.setServicioComida(Boolean.valueOf(no.getTextContent()));
                                break;
                            default:
                                inicializarComun(el, no, r);
                                break;
                        }
                    }
                }
                inicializarCoordenadasComun(e, r);
                lista.add(r);
            }
        }
        return lista;
    }
}