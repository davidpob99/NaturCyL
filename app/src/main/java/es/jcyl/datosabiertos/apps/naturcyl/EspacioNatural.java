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

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class EspacioNatural {
    // TODO
    public static final String URL_CSV = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/espacios_naturales/1284378150075.csv";
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/espacios_naturales/1284378150075.kml";

    private int id;
    private String codigo;
    private String nombre;
    private String fechaDeclaracion;
    private String tipoDeclaracion;
    private ArrayList<GeoPoint> coordenadas;
    private ArrayList<Aparcamiento> aparcamientos;

    public EspacioNatural(int id, String codigo, String nombre, String fechaDeclaracion, String tipoDeclaracion, ArrayList<GeoPoint> coordenadas) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaDeclaracion = fechaDeclaracion;
        this.tipoDeclaracion = tipoDeclaracion;
        this.coordenadas = coordenadas;
    }

    @Override
    public String toString() {
        return "EspacioNatural{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaDeclaracion='" + fechaDeclaracion + '\'' +
                ", tipoDeclaracion='" + tipoDeclaracion + '\'' +
                ", coordenadas=" + coordenadas +
                ", aparcamientos=" + aparcamientos +
                '}';
    }

    public ArrayList<GeoPoint> getCoordenadas() {
        return coordenadas;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaDeclaracion() {
        return fechaDeclaracion;
    }

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public ArrayList<Aparcamiento> getAparcamientos() {
        return aparcamientos;
    }
}
