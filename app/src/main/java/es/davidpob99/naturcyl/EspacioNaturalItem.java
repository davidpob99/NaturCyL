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
 * Características comunes de todos los componentes
 * de los espacios naturales.
 */
public class EspacioNaturalItem {
    public static final String[] estados = {"Bueno", "Sin determinar", "Aceptable", "Malo"};

    private int id;
    private boolean q;
    private String codigo;
    private String observaciones;
    private String fechaEstado;
    private String fechaDeclaracion;
    private int estado;
    private boolean senalizacionExterna;
    private String acceso;
    private String nombre;
    private boolean interesTuristico;
    private double superficie;
    private GeoPoint coordenadas;
    private boolean accesibilidad;

    public EspacioNaturalItem() {

    }

    public EspacioNaturalItem(int id, boolean q, String codigo, String observaciones, String fechaEstado, String fechaDeclaracion, int estado, boolean senalizacionExterna, String acceso, String nombre, boolean interesTuristico, double superficie, GeoPoint coordenadas) {
        this.id = id;
        this.q = q;
        this.codigo = codigo;
        this.observaciones = observaciones;
        this.fechaEstado = fechaEstado;
        this.fechaDeclaracion = fechaDeclaracion;
        this.estado = estado;
        this.senalizacionExterna = senalizacionExterna;
        this.acceso = acceso;
        this.nombre = nombre;
        this.interesTuristico = interesTuristico;
        this.superficie = superficie;
        this.coordenadas = coordenadas;
    }

    @Override
    public String toString() {
        return "EspacioNaturalItem{" +
                "id=" + id +
                ", q=" + q +
                ", codigo='" + codigo + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", fechaEstado='" + fechaEstado + '\'' +
                ", fechaDeclaracion='" + fechaDeclaracion + '\'' +
                ", estado=" + estado +
                ", senalizacionExterna=" + senalizacionExterna +
                ", acceso='" + acceso + '\'' +
                ", nombre='" + nombre + '\'' +
                ", interesTuristico=" + interesTuristico +
                ", superficie=" + superficie +
                ", coordenadas=" + coordenadas +
                ", accesibilidad=" + accesibilidad +
                '}';
    }

    public boolean isAccesibilidad() {
        return accesibilidad;
    }

    public void setAccesibilidad(boolean accesibilidad) {
        this.accesibilidad = accesibilidad;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(String fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public String getFechaDeclaracion() {
        return fechaDeclaracion;
    }

    public void setFechaDeclaracion(String fechaDeclaracion) {
        this.fechaDeclaracion = fechaDeclaracion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isSenalizacionExterna() {
        return senalizacionExterna;
    }

    public void setSenalizacionExterna(boolean senalizacionExterna) {
        this.senalizacionExterna = senalizacionExterna;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isInteresTuristico() {
        return interesTuristico;
    }

    public void setInteresTuristico(boolean interesTuristico) {
        this.interesTuristico = interesTuristico;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public GeoPoint getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(GeoPoint coordenadas) {
        this.coordenadas = coordenadas;
    }
}


