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

/**
 * Estructura techada que se crea para dar cobijo y permitir el descanso y
 * pernoctación durante uno o varios días, generalmente situada en itinerarios de
 * difícil práctica. Cubren las demandas de visitantes en zonas de montaña, alta
 * montaña y otras zonas aisladas o de difícil accesibilidad.
 */
class Refugio extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/refugios/1284378322579.kml";
    static final String[] TIPOS = {"Cabaña", "Chozo o Chivitera", "Caseta", "Casa o Edificación", "Refugio de montaña"};
    static final String[] USOS = {"Libre", "Restringido", "Desuso"};

    private int tipo;
    private int uso;
    private String actividad;
    private boolean capacidadPernoctacion;
    private boolean servicioComida;

    public Refugio() {
        super();
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
