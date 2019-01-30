/*
 * Aplicación para consultar los espacios naturales de la comunidad
 * autónoma de Castilla y León, así como sus equipamientos y
 * posibilidades.
 *
 * Copyright (C) 2019  David Población Criado
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

import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class ListaEspaciosNaturalesItems<E extends EspacioNaturalItem> {
    private ArrayList<E> elementos;
    private ArrayList<E> estanEnEspacio;
    private EspacioNatural espacioNatural;

    public ListaEspaciosNaturalesItems() {
        elementos = new ArrayList<>();
        estanEnEspacio = new ArrayList<>();
    }

    public ListaEspaciosNaturalesItems(ArrayList<E> elementos) {
        this.elementos = new ArrayList<>();
        estanEnEspacio = new ArrayList<>();
        for (E e : elementos) {
            this.elementos.add(e);
        }
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
    public boolean poligonoContiene(GeoPoint gp) {
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