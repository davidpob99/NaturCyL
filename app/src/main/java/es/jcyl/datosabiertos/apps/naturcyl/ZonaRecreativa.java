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

/**
 * Las zonas recreativas son espacios al aire libre que puede incluir dotaciones como suministro de agua, servicio higiénicos, limpieza y recogida de residuos, mesas, bancos y barbacoas, estacionamiento de vehículos, circuitos para el ejercicio físico y juegos infantiles, en el que se pueden realizar diversas actividades recreativas, de ocio y espaiciamiento durante una jornada
 *
 * @author David Población Criado
 */
public class ZonaRecreativa extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/zonas_recreativas/1284378127843.kml";

    private boolean merendero;

    public ZonaRecreativa() {
        super();
    }

    public ZonaRecreativa(int id, boolean q, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, GeoPoint coordenadas, String nombre, boolean merendero) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);

        this.merendero = merendero;
    }

    public boolean isMerendero() {
        return merendero;
    }

    public void setMerendero(boolean merendero) {
        this.merendero = merendero;
    }
}
