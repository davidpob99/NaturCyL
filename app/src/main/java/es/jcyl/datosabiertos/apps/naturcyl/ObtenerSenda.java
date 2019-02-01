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

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class ObtenerSenda extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {
    protected static String GRAPHHOPPER_API_KEY;

    @Override
    protected Road doInBackground(ArrayList<GeoPoint>... arrayLists) {
        RoadManager roadManager = new GraphHopperRoadManager(GRAPHHOPPER_API_KEY, false);
        roadManager.addRequestOption("routeType=pedestrian");
        Road road = roadManager.getRoad(arrayLists[0]);
        return road;
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Road result) {
    }
}
