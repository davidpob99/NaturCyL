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
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private EditText distancia;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private int posicion;
    private ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spinner spinner;

        setContentView(R.layout.activity_cercanos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Cargar Google Play Services
        mGoogleApiClient = new GoogleApiClient.Builder(CercanosActivity.this)
                .addConnectionCallbacks(CercanosActivity.this)
                .addOnConnectionFailedListener(CercanosActivity.this)
                .addApi(LocationServices.API)
                .build();
        // Acción del botón flotante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (posicion) {
                    case 0:
                        listaItems = Utilidades.inicializarAparcamientos(getFilesDir());
                        break;
                    case 1:
                        listaItems = Utilidades.inicializarObservatorios(getFilesDir());
                        break;
                    case 2:
                        listaItems = Utilidades.inicializarMiradores(getFilesDir());
                        break;
                    case 3:
                        listaItems = Utilidades.inicializarZonasRecreativas(getFilesDir());
                        break;
                    case 4:
                        listaItems = Utilidades.inicializarCasasParque(getFilesDir());
                        break;
                    case 5:
                        listaItems = Utilidades.inicializarCentrosVisitantes(getFilesDir());
                        break;
                    case 6:
                        listaItems = Utilidades.inicializarArbolesSingulares(getFilesDir());
                        break;
                    case 7:
                        listaItems = Utilidades.inicializarZonasAcampada(getFilesDir());
                        break;
                    case 8:
                        listaItems = Utilidades.inicializarCampamentos(getFilesDir());
                        break;
                    case 9:
                        listaItems = Utilidades.inicializarRefugios(getFilesDir());
                        break;
                    case 10:
                        listaItems = Utilidades.inicializarQuioscos(getFilesDir());
                        break;
                    default:
                        break;
                }
                double metros;
                try {
                    metros = Double.valueOf(distancia.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e("DIST", "Distancia null", e);
                    new AlertDialog.Builder(CercanosActivity.this)
                            .setTitle("Error")
                            .setMessage("Especifique una distancia en metros por favor")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Continuar con la actividad
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
                    try {
                        if (mLastLocation.distanceTo(loc) <= metros) {
                            listaFinal.add(eni);
                        }
                    } catch (NullPointerException e) {
                        Log.e("DIST", "Distancia null", e);
                        Toast.makeText(CercanosActivity.this, "No se ha podido obtener su ubicación. Compruebe que está activada", Toast.LENGTH_LONG).show();
                    }
                    listaFinal.deElementosAEnEspacio();
                }
                if (listaFinal.getElementos().isEmpty()) {
                    new AlertDialog.Builder(CercanosActivity.this)
                            .setTitle("Vacío")
                            .setMessage("No se han encontrado " + Utilidades.nombresItems[posicion].toLowerCase() + " a menos distancia que la indicada (" + metros + " m).")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Continuar con la actividad
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
                    public void onClickItem(int position) {
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
        if (Utilidades.nombresItems.length - 1 >= 0)
            System.arraycopy(Utilidades.nombresItems, 0, nombres, 0, Utilidades.nombresItems.length - 1);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View vies,
                                       int position, long id) {
                posicion = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // Continuar con la actividad
            }
        });
        // Inicializacion distancia
        distancia = findViewById(R.id.cercanos_metros);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explicación concreta de esta app
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // Comprobar si tenemos permiso
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
        // Continuar con la actividad
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Continuar con la actividad
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
