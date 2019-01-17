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
 * Estructura techada que se crea para dar cobijo y permitir el descanso y
 * pernoctación durante uno o varios días, generalmente situada en itinerarios de
 * difícil práctica. Cubren las demandas de visitantes en zonas de montaña, alta
 * montaña y otras zonas aisladas o de difícil accesibilidad.
 */
public class Refugio extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/refugios/1284378322579.kml";
    public static final String[] tipos = {"Cabaña", "Chozo o Chivitera", "Caseta", "Casa o Edificación", "Refugio de montaña"};
    public static final String[] usos = {"Libre", "Restringido", "Desuso"};

    private int tipo;
    private int uso;
    private String actividad;
    private boolean capacidadPernoctacion;
    private boolean servicioComida;

    public Refugio() {
        super();
    }

    public Refugio(int id, boolean q, String codigo, String observaciones, String fechaEstado, String fechaDeclaracion, int estado, boolean senalizacionExterna, String acceso, String nombre, boolean interesTuristico, double superficie, GeoPoint coordenadas, int tipo, int uso, String actividad, boolean capacidadPernoctacion, boolean servicioComida) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);
        this.tipo = tipo;
        this.uso = uso;
        this.actividad = actividad;
        this.capacidadPernoctacion = capacidadPernoctacion;
        this.servicioComida = servicioComida;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getUso() {
        return uso;
    }

    public void setUso(int uso) {
        this.uso = uso;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public boolean isCapacidadPernoctacion() {
        return capacidadPernoctacion;
    }

    public void setCapacidadPernoctacion(boolean capacidadPernoctacion) {
        this.capacidadPernoctacion = capacidadPernoctacion;
    }

    public boolean isServicioComida() {
        return servicioComida;
    }

    public void setServicioComida(boolean servicioComida) {
        this.servicioComida = servicioComida;
    }
}
