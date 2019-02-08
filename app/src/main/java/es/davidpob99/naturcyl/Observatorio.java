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
 * Estructura, fija o móvil, que se utiliza para la observación
 * de fauna silvestre y permite la ocultación de los visitantes
 * con el objeto de no ahuyentar o perturbar a los animales.
 *
 * @author David Población Criado
 */
class Observatorio extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/observatorios/1284378315734.kml";
    static final String[] TIPOS = {
            "Observatorio de fauna",
            "Observatorio astronómico",
            "Otro"
    };

    private int tipoObservatorio;
    private String entornoObservatorio;

    public Observatorio() {
        super();
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
