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

import java.util.ArrayList;

public class EspacioNatural {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/espacios_naturales/1284378150075.kml";
    public static final String URL_IMG_BASE = "https://idecyl.jcyl.es/sigren/";

    private int id;
    private boolean q;
    private String codigo;
    private String nombre;
    private String fechaDeclaracion;
    private String tipoDeclaracion;
    private ArrayList<GeoPoint> coordenadas;
    private String imagen;
    private ArrayList<Aparcamiento> aparcamientos; //TODO

    public EspacioNatural(int id, boolean q, String codigo, String nombre, String fechaDeclaracion, String tipoDeclaracion, ArrayList<GeoPoint> coordenadas, String imagen) {
        this.id = id;
        this.q = q;
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaDeclaracion = fechaDeclaracion;
        this.tipoDeclaracion = tipoDeclaracion;
        this.coordenadas = coordenadas;
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "EspacioNatural{" +
                "id=" + id +
                ", q=" + q +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaDeclaracion='" + fechaDeclaracion + '\'' +
                ", tipoDeclaracion='" + tipoDeclaracion + '\'' +
                ", coordenadas=" + coordenadas +
                ", imagen='" + imagen + '\'' +
                ", aparcamientos=" + aparcamientos +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isQ() {
        return q;
    }

    public void setQ(boolean q) {
        this.q = q;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeclaracion() {
        return fechaDeclaracion;
    }

    public void setFechaDeclaracion(String fechaDeclaracion) {
        this.fechaDeclaracion = fechaDeclaracion;
    }

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(String tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public ArrayList<GeoPoint> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(ArrayList<GeoPoint> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
