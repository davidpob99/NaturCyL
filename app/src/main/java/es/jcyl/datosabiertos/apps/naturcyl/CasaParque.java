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

public class CasaParque extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/casas_del_parque/1284378132826.kml";
    private boolean servicioInformativo;
    private boolean biblioteca;
    private boolean tiendaVerde;

    public CasaParque() {
        super();
    }

    public CasaParque(int id, boolean q, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, GeoPoint coordenadas, String nombre, String web, boolean servicioInformativo, boolean biblioteca, boolean tiendaVerde) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);

        this.servicioInformativo = servicioInformativo;
        this.biblioteca = biblioteca;
        this.tiendaVerde = tiendaVerde;
    }

    public boolean isServicioInformativo() {
        return servicioInformativo;
    }

    public void setServicioInformativo(boolean servicioInformativo) {
        this.servicioInformativo = servicioInformativo;
    }

    public boolean isBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(boolean biblioteca) {
        this.biblioteca = biblioteca;
    }

    public boolean isTiendaVerde() {
        return tiendaVerde;
    }

    public void setTiendaVerde(boolean tiendaVerde) {
        this.tiendaVerde = tiendaVerde;
    }
}
