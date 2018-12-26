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
 * Estructura, fija o móvil, que se utiliza para la observación
 * de fauna silvestre y permite la ocultación de los visitantes
 * con el objeto de no ahuyentar o perturbar a los animales.
 *
 * @author David Población Criado
 */
public class Observatorio {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/observatorios/1284378315734.kml";
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
    private GeoPoint coordenadas;
    private String nombre;
    private int tipoObservatorio;
    private String entornoObservatorio;

    public Observatorio() {
    }

    public Observatorio(int id, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, double superficie, GeoPoint coordenadas, String nombre, int tipoObservatorio, String entornoObservatorio) {
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
        this.coordenadas = coordenadas;
        this.nombre = nombre;
        this.tipoObservatorio = tipoObservatorio;
        this.entornoObservatorio = entornoObservatorio;
    }

    @Override
    public String toString() {
        return "Observatorio{" +
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
                ", coordenadas=" + coordenadas +
                ", nombre='" + nombre + '\'' +
                ", tipoObservatorio=" + tipoObservatorio +
                ", entornoObservatorio='" + entornoObservatorio + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(String fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public boolean isSenalizacionExterna() {
        return senalizacionExterna;
    }

    public void setSenalizacionExterna(boolean senalizacionExterna) {
        this.senalizacionExterna = senalizacionExterna;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
