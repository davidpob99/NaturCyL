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
 * Espacio habilitado para el estacionamiento de vehículos, que tiene como
 * finalidad facilitar el acceso ordenado de los visitantes con el objeto de
 * disminuir los impactos sobre el entorno.
 *
 * @author David Población Criado
 */
public class Aparcamiento extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/aparcamientos/1284378117797.kml";
    private boolean delimitado;
    private boolean aparcaBicis;

    public Aparcamiento() {
        super();
    }

    public Aparcamiento(int id, boolean q, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, boolean delimitado, boolean aparcaBicis, GeoPoint coordenadas, String nombre) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);
        this.delimitado = delimitado;
        this.aparcaBicis = aparcaBicis;
    }

    public boolean isDelimitado() {
        return delimitado;
    }

    public void setDelimitado(boolean delimitado) {
        this.delimitado = delimitado;
    }

    public boolean isAparcaBicis() {
        return aparcaBicis;
    }

    public void setAparcaBicis(boolean aparcaBicis) {
        this.aparcaBicis = aparcaBicis;
    }
}
