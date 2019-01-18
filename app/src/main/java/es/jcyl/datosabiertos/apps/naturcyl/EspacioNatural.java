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

import android.graphics.Color;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;

public class EspacioNatural {
    public static final String URL_KML = "https://datosabiertos.jcyl.es/web/jcyl/risp/es/medio-ambiente/espacios_naturales/1284378150075.kml";
    public static final String URL_IMG_BASE = "https://idecyl.jcyl.es/sigren/";

    private int id;
    private boolean q;
    private String codigo;
    private String nombre;
    private String fechaDeclaracion;
    private String tipoDeclaracion;
    private ArrayList<GeoPoint> coordenadas;
    private String imagen;
    private ArrayList<EspacioNaturalItem> items;
    private Polygon poligonoCoordenadas;

    public EspacioNatural(int id, boolean q, String codigo, String nombre, String fechaDeclaracion, String tipoDeclaracion, ArrayList<GeoPoint> coordenadas, String imagen) {
        this.id = id;
        this.q = q;
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaDeclaracion = fechaDeclaracion;
        this.tipoDeclaracion = tipoDeclaracion;
        this.coordenadas = coordenadas;
        this.imagen = imagen;
        poligonoCoordenadas = new Polygon();
        poligonoCoordenadas.setFillColor(Color.argb(75, 0, 220, 27));
        poligonoCoordenadas.setPoints(getCoordenadas());
        poligonoCoordenadas.setTitle(getNombre());
    }

    @Override
    public String toString() {
        return "EspacioNatural{" +
                "id=" + id +
                ", q=" + q +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaDeclaracion='" + fechaDeclaracion + '\'' +
                ", tipoDeclaracion='" + tipoDeclaracion + '\'' +
                ", coordenadas=" + coordenadas +
                ", imagen='" + imagen + '\'' +
                '}';
    }

    public boolean estaEnEspacio(EspacioNaturalItem eni) {
        GeoPoint coordenadasEni = eni.getCoordenadas();
        double xMin = coordenadas.get(0).getLatitude();
        double yMin = coordenadas.get(0).getLongitude();
        double xMax = xMin;
        double yMax = yMin;

        for (GeoPoint gp : coordenadas) {
            if (xMin > gp.getLatitude()) {
                xMin = gp.getLatitude();
            }
            if (xMax < gp.getLatitude()) {
                xMax = gp.getLatitude();
            }
            if (yMin > gp.getLongitude()) {
                yMin = gp.getLongitude();
            }
            if (yMax < gp.getLongitude()) {
                yMax = gp.getLongitude();
            }
        }
        return coordenadasEni.getLatitude() <= xMax && coordenadasEni.getLatitude() >= xMin && coordenadasEni.getLongitude() <= yMax && coordenadasEni.getLongitude() >= yMin;
    }

    public boolean hayAparcamiento() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Aparcamiento) {
                return true;
            }
        }
        return false;
    }

    public boolean hayArbolSingular() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof ArbolSingular) {
                return true;
            }
        }
        return false;
    }

    public boolean hayCampamento() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Campamento) {
                return true;
            }
        }
        return false;
    }

    public boolean hayCasaParque() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof CasaParque) {
                return true;
            }
        }
        return false;
    }

    public boolean hayCentroVisitante() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof CentroVisitante) {
                return true;
            }
        }
        return false;
    }

    public boolean hayMirador() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Mirador) {
                return true;
            }
        }
        return false;
    }

    public boolean hayObservatorio() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Observatorio) {
                return true;
            }
        }
        return false;
    }

    public boolean hayRefugio() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof Refugio) {
                return true;
            }
        }
        return false;
    }

    public boolean hayZonaAcampada() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof ZonaAcampada) {
                return true;
            }
        }
        return false;
    }

    public boolean hayZonaRecreativa() {
        for (EspacioNaturalItem eni : items) {
            if (eni instanceof ZonaRecreativa) {
                return true;
            }
        }
        return false;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeclaracion() {
        return fechaDeclaracion;
    }

    public void setFechaDeclaracion(String fechaDeclaracion) {
        this.fechaDeclaracion = fechaDeclaracion;
    }

    public String getTipoDeclaracion() {
        return tipoDeclaracion;
    }

    public void setTipoDeclaracion(String tipoDeclaracion) {
        this.tipoDeclaracion = tipoDeclaracion;
    }

    public ArrayList<GeoPoint> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(ArrayList<GeoPoint> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public ArrayList<EspacioNaturalItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<EspacioNaturalItem> items) {
        this.items = items;
    }

    public Polygon getPoligonoCoordenadas() {
        return poligonoCoordenadas;
    }
}
