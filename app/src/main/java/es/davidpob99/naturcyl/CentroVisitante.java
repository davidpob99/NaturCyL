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
 * Equipamiento que es punto de referencia de toda la oferta de uso público y está
 * destinado a cumplir los servicios de recepción, información e interpretación
 * relacionados con el espacio natural protegido, sus valores naturales y culturales,
 * y su gestión, así como su orientación para la visita mediante información de la
 * oferta de uso público, y de promoción y desarrollo de programas de actividades y
 * servicios vinculados al uso público y a la educación ambiental. La información e
 * interpretación se realiza tanto con atención personalizada como con
 * exposiciones interpretativas.
 */
class CentroVisitante extends EspacioNaturalItem {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/centros_de_visitantes/1284378137727.kml";
    static final String[] TIPOS = {
            "Aula de la Naturaleza",
            "Aula del río",
            "Centro de Interpretación",
            "Centro temático",
            "Fauna silvestre",
            "Jardín botánico",
            "Oficina comarcal",
            "Punto de información",
            "Otro"
    };
    private int tipo;
    private String descripcion;

    public CentroVisitante() {
        super();
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
