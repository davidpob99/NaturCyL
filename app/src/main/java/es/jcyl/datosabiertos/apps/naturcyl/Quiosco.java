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

/**
 * Instalaciones incluidas como infraestructuras de uso
 * público destinadas a la venta de refrescos…etc.
 */
public class Quiosco extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/quioscos/1284378321847.kml";

    public Quiosco() {
        super();
    }

    public Quiosco(int id, boolean q, String codigo, String observaciones, String fechaEstado, String fechaDeclaracion, int estado, boolean senalizacionExterna, String acceso, String nombre, boolean interesTuristico, double superficie, GeoPoint coordenadas) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);
    }
}
