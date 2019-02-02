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
 * Espacio debidamente delimitado y acondicionado para permitir la instalación de
 * tiendas de campaña por breves periodos de tiempo.
 */
public class ZonaAcampada extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/zonas_acampada/1284378323193.kml";

    public ZonaAcampada() {
        super();
    }

    public ZonaAcampada(int id, boolean q, String codigo, String observaciones, String fechaEstado, String fechaDeclaracion, int estado, boolean senalizacionExterna, String acceso, String nombre, boolean interesTuristico, double superficie, GeoPoint coordenadas) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);
    }
}
