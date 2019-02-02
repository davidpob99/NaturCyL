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
 * Equipamiento que es punto de referencia de toda la oferta de uso público y está
 * destinado a cumplir los servicios de recepción, información e interpretación
 * relacionados con el espacio natural protegido, sus valores naturales y culturales,
 * y su gestión, así como su orientación para la visita mediante información de la
 * oferta de uso público, y de promoción y desarrollo de programas de actividades y
 * servicios vinculados al uso público y a la educación ambiental. La información e
 * interpretación se realiza tanto con atención personalizada como con
 * exposiciones interpretativas.
 */
public class CentroVisitante extends CasaParque {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/centros_de_visitantes/1284378137727.kml";
    public static final String[] TIPOS = {
            "Aula de la Naturaleza",
            "Aula del río",
            "Centro de Interpretación",
            "Centro temático",
            "Fauna silvestre",
            "Jardín botánico",
            "Oficina comarcal",
            "Punto de información",
            "Otro"
    };
    private int tipo;
    private String descripcion;

    public CentroVisitante() {
        super();
    }

    public CentroVisitante(int id, boolean q, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, GeoPoint coordenadas, String nombre, String web, boolean servicioInformativo, boolean biblioteca, boolean tiendaVerde, int tipo, String descripcion) {
        super(id, q, codigo, fechaDeclaracion, estado, fechaEstado, senalizacionExterna, observaciones, acceso, interesTuristico, superficie, coordenadas, nombre, web, servicioInformativo, biblioteca, tiendaVerde);
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
