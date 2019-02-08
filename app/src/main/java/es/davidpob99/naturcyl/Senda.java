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

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Senda extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/sendas/1284378322994.kml";
    public static final String[] DIFICULTAD = {
            "Media",
            "Sin determinar",
            "Alta",
            "Baja"
    };
    public static final int[] COLORES_DIFICULTAD = {
            android.R.color.holo_orange_dark,
            android.R.color.darker_gray,
            android.R.color.holo_red_dark,
            android.R.color.holo_green_dark
    };

    private int tipo;
    private double longitud;
    private int tiempoRecorrido;
    private double ciclabilidad;
    private String codigoSenda;
    private int dificultad;
    private int desnivel;
    private int tipoOficial;
    private ArrayList<GeoPoint> coordenadasSenda;

    public Senda() {
        super();
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getTiempoRecorrido() {
        return tiempoRecorrido;
    }

    public void setTiempoRecorrido(int tiempoRecorrido) {
        this.tiempoRecorrido = tiempoRecorrido;
    }

    public double getCiclabilidad() {
        return ciclabilidad;
    }

    public void setCiclabilidad(double ciclabilidad) {
        this.ciclabilidad = ciclabilidad;
    }

    public String getCodigoSenda() {
        return codigoSenda;
    }

    public void setCodigoSenda(String codigoSenda) {
        this.codigoSenda = codigoSenda;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getDesnivel() {
        return desnivel;
    }

    public void setDesnivel(int desnivel) {
        this.desnivel = desnivel;
    }

    public int getTipoOficial() {
        return tipoOficial;
    }

    public void setTipoOficial(int tipoOficial) {
        this.tipoOficial = tipoOficial;
    }

    public ArrayList<GeoPoint> getCoordenadasSenda() {
        return coordenadasSenda;
    }

    public void setCoordenadasSenda(ArrayList<GeoPoint> coordenadasSenda) {
        this.coordenadasSenda = coordenadasSenda;
    }
}
