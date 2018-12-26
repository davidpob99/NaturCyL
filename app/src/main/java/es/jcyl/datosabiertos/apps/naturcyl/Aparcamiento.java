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
public class Aparcamiento {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/aparcamientos/1284378117797.kml";

    private int id;
    private String codigo;
    private String fechaDeclaracion;
    private int estado;
    private String fechaEstado;
    private boolean senalizacionExterna;
    private String observaciones;
    private String acceso;
    private boolean interesTuristico;
    private double superficie;
    private boolean delimitado;
    private boolean aparcaBicis;
    private GeoPoint coordenadas;
    private String nombre;

    public Aparcamiento() {
    }

    public Aparcamiento(int id, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, boolean delimitado, boolean aparcaBicis, GeoPoint coordenadas, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.fechaDeclaracion = fechaDeclaracion;
        this.estado = estado;
        this.fechaEstado = fechaEstado;
        this.senalizacionExterna = senalizacionExterna;
        this.observaciones = observaciones;
        this.acceso = acceso;
        this.interesTuristico = interesTuristico;
        this.superficie = superficie;
        this.delimitado = delimitado;
        this.aparcaBicis = aparcaBicis;
        this.coordenadas = coordenadas;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Aparcamiento{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fechaDeclaracion='" + fechaDeclaracion + '\'' +
                ", estado=" + estado +
                ", fechaEstado='" + fechaEstado + '\'' +
                ", senalizacionExterna=" + senalizacionExterna +
                ", observaciones='" + observaciones + '\'' +
                ", acceso='" + acceso + '\'' +
                ", interesTuristico=" + interesTuristico +
                ", superficie=" + superficie +
                ", delimitado=" + delimitado +
                ", aparcaBicis=" + aparcaBicis +
                ", coordenadas=" + coordenadas +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public GeoPoint getCoordenadas() {
        return coordenadas;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getFechaDeclaracion() {
        return fechaDeclaracion;
    }

    public int getEstado() {
        return estado;
    }

    public String getFechaEstado() {
        return fechaEstado;
    }

    public boolean isSenalizacionExterna() {
        return senalizacionExterna;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getAcceso() {
        return acceso;
    }

    public boolean isInteresTuristico() {
        return interesTuristico;
    }

    public void setCoordenadas(GeoPoint coordenadas) {
        this.coordenadas = coordenadas;
    }

    public boolean isDelimitado() {
        return delimitado;
    }

    public boolean isAparcaBicis() {
        return aparcaBicis;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setFechaDeclaracion(String fechaDeclaracion) {
        this.fechaDeclaracion = fechaDeclaracion;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setFechaEstado(String fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public void setSenalizacionExterna(boolean senalizacionExterna) {
        this.senalizacionExterna = senalizacionExterna;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public void setInteresTuristico(boolean interesTuristico) {
        this.interesTuristico = interesTuristico;
    }

    public void setDelimitado(boolean delimitado) {
        this.delimitado = delimitado;
    }

    public void setAparcaBicis(boolean aparcaBicis) {
        this.aparcaBicis = aparcaBicis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
