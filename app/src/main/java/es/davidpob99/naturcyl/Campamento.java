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
class Campamento extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/campamentos_turismo/1284378128806.kml";
    static final String[] TIPOS = {
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
