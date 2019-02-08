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

import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class ListaEspaciosNaturalesItems<E extends EspacioNaturalItem> {
    private final ArrayList<E> elementos;
    private final ArrayList<E> estanEnEspacio;
    private EspacioNatural espacioNatural;

    public ListaEspaciosNaturalesItems() {
        elementos = new ArrayList<>();
        estanEnEspacio = new ArrayList<>();
    }

    public ListaEspaciosNaturalesItems(ArrayList<E> elementos) {
        this.elementos = new ArrayList<>();
        estanEnEspacio = new ArrayList<>();
        this.elementos.addAll(elementos);
    }

    public void deElementosAEnEspacio() {
        estanEnEspacio.addAll(elementos);
    }

    public ArrayList<E> getElementos() {
        return elementos;
    }

    public ArrayList<E> getEstanEnEspacio() {
        return estanEnEspacio;
    }

    public EspacioNatural getEspacioNatural() {
        return espacioNatural;
    }

    public void setEspacioNatural(EspacioNatural espacioNatural) {
        this.espacioNatural = espacioNatural;
    }

    public E get(int posicion) {
        return elementos.get(posicion);
    }

    public void add(E e) {
        elementos.add(e);
    }

    public int size() {
        return elementos.size();
    }

    public void actualizarEnEspacio() {
        for (E e : elementos) {
            try {
                if (e.getCodigo().substring(0, 3).equals(espacioNatural.getCodigo()) ||
                        (poligonoContiene(e.getCoordenadas()) && !estanEnEspacio.contains(e))) {
                    estanEnEspacio.add(e);
                }
            } catch (NullPointerException error) {
                Log.e("ERROR", error.toString());
            }
        }
        // Arreglar problema de códigos como el Parque Nacional y Parque Regional
        for (E e : elementos) {
            for (int i = 0; i < estanEnEspacio.size(); i++) {
                E f = estanEnEspacio.get(i);
                try {
                    if (f.getCodigo().substring(0, 3).equals(e.getCodigo().substring(0, 3)) && !estanEnEspacio.contains(e)) {
                        estanEnEspacio.add(e);
                    }
                } catch (NullPointerException error) {
                    Log.e("ERROR", error.toString());
                }

            }
        }


    }

    /**
     * Return true if the given point is contained inside the boundary.
     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     *
     * @param gp The point to check
     * @return true if the point is inside the boundary, false otherwise
     */
    private boolean poligonoContiene(GeoPoint gp) {
        int i;
        int j;
        boolean result = false;

        for (i = 0, j = espacioNatural.getCoordenadas().size() - 1; i < espacioNatural.getCoordenadas().size(); j = i++) {
            if ((espacioNatural.getCoordenadas().get(i).getLongitude() > gp.getLongitude()) != (espacioNatural.getCoordenadas().get(j).getLongitude() > gp.getLongitude()) &&
                    (gp.getLatitude() < (espacioNatural.getCoordenadas().get(j).getLatitude() - espacioNatural.getCoordenadas().get(i).getLatitude()) * (gp.getLongitude() - espacioNatural.getCoordenadas().get(i).getLongitude()) /
                            (espacioNatural.getCoordenadas().get(j).getLongitude() - espacioNatural.getCoordenadas().get(i).getLongitude()) + espacioNatural.getCoordenadas().get(i).getLatitude())) {
                result = !result;
            }
        }
        return result;
    }
}