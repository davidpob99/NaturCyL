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

package es.davidpob99.naturcyl;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class CercanosActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private Spinner spinner;
    private EditText distancia;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private int posicion;
    private ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cercanos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargar Google Play Services
        mGoogleApiClient = new GoogleApiClient.Builder(CercanosActivity.this)
                .addConnectionCallbacks(CercanosActivity.this)
                .addOnConnectionFailedListener(CercanosActivity.this)
                .addApi(LocationServices.API)
                .build();

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
                    Log.i("DIST", String.valueOf(mLastLocation));

                    try {
                        if (mLastLocation.distanceTo(loc) <= metros) {
                            listaFinal.add(eni);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(CercanosActivity.this, "No se ha podido obtener su ubicación. Compruebe que está activada", Toast.LENGTH_LONG).show();
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

        // Spinner inicializacion
        spinner = findViewById(R.id.cercanos_spinner);
        String[] nombres = new String[Utilidades.nombresItems.length - 1];
        for (int i = 0; i < Utilidades.nombresItems.length - 1; i++) {
            nombres[i] = Utilidades.nombresItems[i];
        }
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombres));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View vies,
                                       int position, long id) {
                posicion = position;
                // Log.i("INFO", String.valueOf(posicion));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        // Inicializacion distancia
        distancia = findViewById(R.id.cercanos_metros);
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
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationPermission();
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        //hacer cosas
                    } else {
                        Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Permisos no otorgados", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void mensajeNoGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Parece que su GPS está desactivado, ¿le gustaría activarlo?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Toast.makeText(CercanosActivity.this, "Lógicamente no se pueden obtener los lugares más cercanos sin su ubicación", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationPermission();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // Trabajar con coordenadas
        } else {
            Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

}
