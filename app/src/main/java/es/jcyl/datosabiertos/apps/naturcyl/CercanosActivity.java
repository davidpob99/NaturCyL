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

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CercanosActivity extends AppCompatActivity implements LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    String provider;
    Location localizacion;
    private Spinner spinner;
    private EditText distancia;
    private int posicion;
    private ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cercanos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (posicion) {
                    case 0:
                        listaItems = Utilidades.inicializarAparcamientos(getApplicationContext().getFilesDir());
                        break;
                    case 1:
                        listaItems = Utilidades.inicializarObservatorios(getApplicationContext().getFilesDir());
                        break;
                    case 2:
                        listaItems = Utilidades.inicializarMiradores(getApplicationContext().getFilesDir());
                        break;
                    case 3:
                        listaItems = Utilidades.inicializarZonasRecreativas(getApplicationContext().getFilesDir());
                        break;
                    case 4:
                        listaItems = Utilidades.inicializarCasasParque(getApplicationContext().getFilesDir());
                        break;
                    case 5:
                        listaItems = Utilidades.inicializarCentrosVisitantes(getApplicationContext().getFilesDir());
                        break;
                    case 6:
                        listaItems = Utilidades.inicializarArbolesSingulares(getApplicationContext().getFilesDir());
                        break;
                    case 7:
                        listaItems = Utilidades.inicializarZonasAcampada(getApplicationContext().getFilesDir());
                        break;
                    case 8:
                        listaItems = Utilidades.inicializarCampamentos(getApplicationContext().getFilesDir());
                        break;
                    case 9:
                        listaItems = Utilidades.inicializarRefugios(getApplicationContext().getFilesDir());
                        break;
                    case 10:
                        listaItems = Utilidades.inicializarQuioscos(getApplicationContext().getFilesDir());
                        break;
                }
                double metros;
                try {
                    metros = Double.valueOf(distancia.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(CercanosActivity.this)
                            .setTitle("Error")
                            .setMessage("Especifique una distancia en metros por favor")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                    return;

                }

                final ListaEspaciosNaturalesItems<EspacioNaturalItem> listaFinal = new ListaEspaciosNaturalesItems<>();
                for (int i = 0; i < listaItems.getElementos().size(); i++) {
                    Location loc = new Location("");
                    EspacioNaturalItem eni = listaItems.getElementos().get(i);
                    loc.setLatitude(eni.getCoordenadas().getLatitude());
                    loc.setLongitude(eni.getCoordenadas().getLongitude());
                    // Log.i("DIST", String.valueOf(localizacion.distanceTo(loc)));
                    if (localizacion.distanceTo(loc) <= metros) {
                        listaFinal.add(eni);
                    }
                    listaFinal.deElementosAEnEspacio();
                }

                if (listaFinal.getElementos().size() == 0) {
                    new AlertDialog.Builder(CercanosActivity.this)
                            .setTitle("Vacío")
                            .setMessage("No se han encontrado " + Utilidades.nombresItems[posicion].toLowerCase() + " a menos distancia que la indicada (" + metros + " m).")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                    return;
                }

                // Cargar RecyclerView
                RecyclerView rv = findViewById(R.id.cercanos_rv);
                rv.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(CercanosActivity.this);
                rv.setLayoutManager(llm);
                RVAdapterItem adapter = new RVAdapterItem(listaFinal.getElementos(), new RVClickListenerEspacio() {
                    @Override
                    public void onClickItem(View v, int position) {
                        ItemActivity.lista = listaFinal;
                        Intent myIntent = new Intent(CercanosActivity.this, ItemActivity.class);
                        myIntent.putExtra("tipo", String.valueOf(posicion));
                        myIntent.putExtra("posicion", String.valueOf(position));
                        startActivity(myIntent);
                    }
                });
                rv.setAdapter(adapter);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        checkLocationPermission();

        // Spinner inicializacion
        spinner = findViewById(R.id.cercanos_spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.nombresItems));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View vies,
                                       int position, long id) {
                posicion = position;
                // Log.i("INFO", String.valueOf(posicion));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // Mensaje error
            }
        });
        // Inicializacion distancia
        distancia = findViewById(R.id.cercanos_metros);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        localizacion = location;

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        Log.i("Location info: Lat", lat.toString());
        Log.i("Location info: Lng", lng.toString());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Ubicación")
                        .setMessage("Es necesaria la ubicación para calcular los elementos más cercanos")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CercanosActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        provider = locationManager.getBestProvider(new Criteria(), false);
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    new AlertDialog.Builder(CercanosActivity.this)
                            .setTitle("Ubicación")
                            .setMessage("No se ha permitido el uso de la ubicación, por lo que no se pueden encontrar los elementos más cercanos.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            })
                            .create()
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


}
