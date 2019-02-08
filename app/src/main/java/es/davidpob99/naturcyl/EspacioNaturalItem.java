/*
 * Aplicación para consultar los espacios naturales de la comunidad
 * autónoma de Castilla y León, así como sus equipamientos y
 * posibilidades.
 *
 * Copyright (C) 2019  David Población Criado
 *
 * Este programa es software libre: puede redistribuirlo y/o modificarlo bajo
 * los términos de la Licencia General Pública de GNU publicada por la Free
 * Software Foundation, ya sea la versión 3 de la Licencia, o (a su elección)
 * cualquier versión posterior.\n\n
 *
 * Este programa se distribuye con la esperanza de que sea útil pero SIN
 * NINGUNA GARANTÍA; incluso sin la garantía implícita de MERCANTIBILIDAD o
 * CALIFICADA PARA UN PROPÓSITO EN PARTICULAR. Vea la Licencia General Pública
 * de GNU para más detalles.\n\n
 *
 * Usted ha debido de recibir una copia de la Licencia General Pública
 * de GNU junto con este programa. Si no, vea http://www.gnu.org/licenses/
 */

package es.davidpob99.naturcyl;

import android.support.annotation.NonNull;

import org.osmdroid.util.GeoPoint;

/**
 * Características comunes de todos los componentes
 * de los espacios naturales.
 */
class EspacioNaturalItem {
    static final String[] estados = {"Bueno", "Sin determinar", "Aceptable", "Malo"};

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

    EspacioNaturalItem() {
        super();
    }

    @NonNull
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


