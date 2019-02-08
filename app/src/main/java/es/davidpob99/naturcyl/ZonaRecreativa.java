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
 * Las zonas recreativas son espacios al aire libre que puede incluir dotaciones como suministro de agua, servicio higiénicos, limpieza y recogida de residuos, mesas, bancos y barbacoas, estacionamiento de vehículos, circuitos para el ejercicio físico y juegos infantiles, en el que se pueden realizar diversas actividades recreativas, de ocio y espaiciamiento durante una jornada
 *
 * @author David Población Criado
 */
class ZonaRecreativa extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/zonas_recreativas/1284378127843.kml";

    private boolean merendero;

    public ZonaRecreativa() {
        super();
    }

    public boolean isMerendero() {
        return merendero;
    }

    public void setMerendero(boolean merendero) {
        this.merendero = merendero;
    }
}
