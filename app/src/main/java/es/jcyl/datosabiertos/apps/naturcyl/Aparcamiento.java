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

/**
 * Espacio habilitado para el estacionamiento de vehículos, que tiene como
 * finalidad facilitar el acceso ordenado de los visitantes con el objeto de
 * disminuir los impactos sobre el entorno.
 */
public class Aparcamiento {
    public static final String URL_CSV = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/aparcamientos/1284378117797.csv";
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
    private int superficie;
    private boolean delimitado;
    private boolean aparcaBicis;

    public Aparcamiento(int id, String codigo, String fechaDeclaracion, int estado, String fechaEstado, boolean senalizacionExterna, String observaciones, String acceso, boolean interesTuristico, int superficie, boolean delimitado, boolean aparcaBicis) {
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
    }

    public int getId() {
        return id;
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

    public int getSuperficie() {
        return superficie;
    }

    public boolean isDelimitado() {
        return delimitado;
    }

    public boolean isAparcaBicis() {
        return aparcaBicis;
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
                '}';
    }
    /*
    public static void main(String[] args) {

        KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseKMLUrl(URL_KML);
        System.out.println(kmlDocument.toString());

        URL stockURL = new URL(URL_CSV);
        BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
        String s = null;
        while ((s = in.readLine()) != null) {
            String[] tokens = s.split(",");
            Aparcamiento a=null;
            try {
                a = new Aparcamiento(Integer.parseInt(tokens[1]), tokens[6], tokens[8].split("T")[0], Integer.parseInt(tokens[10]), tokens[9].split("T")[0], Boolean.parseBoolean(tokens[11]), tokens[7], tokens[12], Boolean.parseBoolean(tokens[17]), Integer.parseInt(tokens[18]), Boolean.parseBoolean(tokens[22]), Boolean.parseBoolean(tokens[24]));
                System.out.println(a);
            }catch(Exception e){
                e.printStackTrace();
            }
    }*/
}
