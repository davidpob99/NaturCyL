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

import org.osmdroid.util.GeoPoint;

/**
 * Los miradores son espacios acondicionados por su ubicación en un punto de interés paisajístico y por su acceso, facilita la contemplación e interpretación de una vista panorámica o de elementos singulares del paisaje de manera sencilla. Normalmente se ubican al aire libre, aunque puede estar cubierto o formar parte de una estructura edificada
 *
 * @author David Población Criado
 */
public class Mirador extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/miradores/1284378314940.kml";
    private String entorno;

    public Mirador() {
        super();
    }

    public Mirador(int id, boolean q, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, GeoPoint coordenadas, String nombre, String entorno) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);
        this.entorno = entorno;
    }

    public String getEntorno() {
        return entorno;
    }

    public void setEntorno(String entorno) {
        this.entorno = entorno;
    }
}
