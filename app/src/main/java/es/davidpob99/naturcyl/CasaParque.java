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

class CasaParque extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/casas_del_parque/1284378132826.kml";
    private boolean servicioInformativo;
    private boolean biblioteca;
    private boolean tiendaVerde;
    private String web;

    public CasaParque() {
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
