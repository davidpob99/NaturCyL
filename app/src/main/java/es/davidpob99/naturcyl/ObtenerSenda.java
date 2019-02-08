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

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

class ObtenerSenda extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {
    static String GRAPHHOPPER_API_KEY;

    @SafeVarargs
    @Override
    protected final Road doInBackground(ArrayList<GeoPoint>... arrayLists) {
        RoadManager roadManager = new GraphHopperRoadManager(GRAPHHOPPER_API_KEY, false);
        roadManager.addRequestOption("routeType=pedestrian");
        return roadManager.getRoad(arrayLists[0]);
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Road result) {
    }
}
