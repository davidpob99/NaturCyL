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
 * Los Campamentos Juveniles son equipamientos al aire libre en los que el
 * alojamiento se realiza generalmente mediante tiendas de campaña u otros
 * elementos portátiles similares, estando dotados de unos elementos básicos fijos,
 * debidamente preparados para el desarrollo de actividades de tiempo libre,
 * culturales o educativas. Estas instalaciones pretenden ofrecer lugares
 * debidamente acondicionados, en un contacto directo con la naturaleza y la
 * diversidad medio-ambiental, para que los jóvenes, a través de sus Asociaciones
 * Juveniles, Consejos de Juventud, Ayuntamientos, Colectivos Juveniles, etc.
 * puedan realizar sus actividades y prestar servicios a la juventud (Orden
 * ADM/1015/2008, de 4 de junio, por la que se aprueba la Carta de Servicios al
 * Ciudadano de la Red de Campamentos Juveniles de Castilla y León. BOCyL, 17
 * de junio de 2008, núm 115, p.11878).
 * <p>
 * ----------------------------------------------------------------------------------
 * <p>
 * Equipamiento turístico en un espacio al aire libre, destinado a facilitar, mediante
 * pago, la estancia temporal de usuarios en tiendas de campaña, remolques
 * habitables, caravanas y cualquier elemnto móvil similar. Esta dotado con
 * instalaciones y servicios que facilitan su utiicación y, en algunos casos, con
 * instalaciones de carácter permanete como pueden ser casa prefabricadas
 */
public class Campamento extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/campamentos_turismo/1284378128806.kml";
    public static final String[] TIPOS = {
            "Juvenil",
            "Turístico",
            "Turístico",
            "No especificado"
    };

    private boolean servicioInformativo;
    private int cabanas;
    private int parcelas;
    private int tipoCamping; // 1: juvenil, 2 o 3: turistico
    private String web;

    public Campamento() {
        super();
    }

    public Campamento(int id, boolean q, String codigo, String observaciones, String fechaEstado, String fechaDeclaracion, int estado, boolean senalizacionExterna, String acceso, String nombre, boolean interesTuristico, double superficie, GeoPoint coordenadas, boolean servicioInformativo, int cabanas, int parcelas, int tipoCamping, String web) {
        super(id, q, codigo, observaciones, fechaEstado, fechaDeclaracion, estado, senalizacionExterna, acceso, nombre, interesTuristico, superficie, coordenadas);
        this.servicioInformativo = servicioInformativo;
        this.cabanas = cabanas;
        this.parcelas = parcelas;
        this.tipoCamping = tipoCamping;
        this.web = web;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public boolean isServicioInformativo() {
        return servicioInformativo;
    }

    public void setServicioInformativo(boolean servicioInformativo) {
        this.servicioInformativo = servicioInformativo;
    }

    public int getCabanas() {
        return cabanas;
    }

    public void setCabanas(int cabanas) {
        this.cabanas = cabanas;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public int getTipoCamping() {
        return tipoCamping;
    }

    public void setTipoCamping(int tipoCamping) {
        this.tipoCamping = tipoCamping;
    }
}
