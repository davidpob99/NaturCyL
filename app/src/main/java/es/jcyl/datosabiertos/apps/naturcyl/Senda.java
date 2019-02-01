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

    @Override
    public String toString() {
        return "Senda{" +
                "tipo=" + tipo +
                ", longitud=" + longitud +
                ", tiempoRecorrido=" + tiempoRecorrido +
                ", ciclabilidad=" + ciclabilidad +
                ", codigoSenda='" + codigoSenda + '\'' +
                ", dificultad=" + dificultad +
                ", desnivel=" + desnivel +
                ", tipoOficial=" + tipoOficial +
                ", coordenadasSenda=" + coordenadasSenda +
                '}';
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
