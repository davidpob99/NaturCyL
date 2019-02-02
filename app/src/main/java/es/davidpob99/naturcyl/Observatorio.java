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
 * Estructura, fija o móvil, que se utiliza para la observación
 * de fauna silvestre y permite la ocultación de los visitantes
 * con el objeto de no ahuyentar o perturbar a los animales.
 *
 * @author David Población Criado
 */
public class Observatorio extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/observatorios/1284378315734.kml";
    public static final String[] TIPOS = {
            "Observatorio de fauna",
            "Observatorio astronómico",
            "Otro"
    };

    private int tipoObservatorio;
    private String entornoObservatorio;

    public Observatorio() {
        super();
    }

    public Observatorio(int id, boolean q, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, GeoPoint coordenadas, String nombre, int tipoObservatorio, String entornoObservatorio) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);

        this.tipoObservatorio = tipoObservatorio;
        this.entornoObservatorio = entornoObservatorio;
    }

    public int getTipoObservatorio() {
        return tipoObservatorio;
    }

    public void setTipoObservatorio(int tipoObservatorio) {
        this.tipoObservatorio = tipoObservatorio;
    }

    public String getEntornoObservatorio() {
        return entornoObservatorio;
    }

    public void setEntornoObservatorio(String entornoObservatorio) {
        this.entornoObservatorio = entornoObservatorio;
    }
}
