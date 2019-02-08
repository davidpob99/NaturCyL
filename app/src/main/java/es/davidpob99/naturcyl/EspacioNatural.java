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

import android.graphics.Color;
import android.support.annotation.NonNull;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polygon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EspacioNatural implements Comparable<EspacioNatural> {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/espacios_naturales/1284378150075.kml";
    public static final String URL_IMG_BASE = "https://idecyl.jcyl.es/sigren/";

    private int id;
    private boolean q;
    private String codigo;
    private String nombre;
    private final Date fechaDeclaracion;
    private String tipoDeclaracion;
    private ArrayList<GeoPoint> coordenadas;
    private String imagen;
    private ArrayList<EspacioNaturalItem> items;
    private final Polygon poligonoCoordenadas;

    public EspacioNatural(int id, boolean q, String codigo, String nombre, String fechaDeclaracion, String tipoDeclaracion, ArrayList<GeoPoint> coordenadas, String imagen) {
        this.id = id;
        this.q = q;
        this.codigo = codigo;
        this.nombre = nombre;

        String[] date = fechaDeclaracion.split("-");
        this.fechaDeclaracion = new Date(Integer.valueOf(date[0]),
                Integer.valueOf(date[1]),
                Integer.valueOf(date[2]));
        this.tipoDeclaracion = tipoDeclaracion;
        this.coordenadas = coordenadas;
        this.imagen = imagen;
        poligonoCoordenadas = new Polygon();
        poligonoCoordenadas.setFillColor(Color.argb(75, 205, 220, 57));
        poligonoCoordenadas.setPoints(getCoordenadas());
        poligonoCoordenadas.setTitle(getNombre());
    }

    @Override
    public int compareTo(EspacioNatural espacioNatural) {
        return nombre.compareTo(espacioNatural.getNombre());
    }

    @NonNull
    @Override
    public String toString() {
        return "EspacioNatural{" +
                "id=" + id +
                ", q=" + q +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaDeclaracion='" + fechaDeclaracion + '\'' +
                ", tipoDeclaracion='" + tipoDeclaracion + '\'' +
                ", coordenadas=" + coordenadas +
                ", imagen='" + imagen + '\'' +
                '}';
    }

    public boolean hayAparcamiento() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Aparcamiento) {
                return true;
            }
        }
        return false;
    }

    public boolean hayArbolSingular() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof ArbolSingular) {
                return true;
            }
        }
        return false;
    }

    public boolean hayCampamento() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Campamento) {
                return true;
            }
        }
        return false;
    }

    public boolean hayCasaParque() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof CasaParque) {
                return true;
            }
        }
        return false;
    }

    public boolean hayCentroVisitante() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof CentroVisitante) {
                return true;
            }
        }
        return false;
    }

    public boolean hayMirador() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Mirador) {
                return true;
            }
        }
        return false;
    }

    public boolean hayObservatorio() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Observatorio) {
                return true;
            }
        }
        return false;
    }

    public boolean hayRefugio() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Refugio) {
                return true;
            }
        }
        return false;
    }

    public boolean hayZonaAcampada() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof ZonaAcampada) {
                return true;
            }
        }
        return false;
    }

    public boolean hayZonaRecreativa() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof ZonaRecreativa) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isQ() {
        return q;
    }

    public void setQ(boolean q) {
        this.q = q;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeclaracion() {
        SimpleDateFormat formateador = new SimpleDateFormat(
                "d 'de' MMMM 'de '", new Locale("es", "ES"));
        return formateador.format(fechaDeclaracion) + fechaDeclaracion.getYear();
    }

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(String tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public ArrayList<GeoPoint> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(ArrayList<GeoPoint> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public ArrayList<EspacioNaturalItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<EspacioNaturalItem> items) {
        this.items = items;
    }

    public Polygon getPoligonoCoordenadas() {
        return poligonoCoordenadas;
    }
}
