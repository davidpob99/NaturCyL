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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class ListaItemsActivity extends AppCompatActivity {
    static EspacioNatural espacioNatural;
    private int posicion;
    private ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaTmp = new ListaEspaciosNaturalesItems<>();
        listaItems = new ListaEspaciosNaturalesItems<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_items);

        //Obtener de EspacioActivity
        posicion = Integer.valueOf(getIntent().getStringExtra("posicion"));

        setTitle(Utilidades.nombresItems[posicion]);

        switch (posicion) {
            case 0:
                listaTmp = Utilidades.inicializarAparcamientos(getApplicationContext().getFilesDir());
                break;
            case 1:
                listaTmp = Utilidades.inicializarObservatorios(getApplicationContext().getFilesDir());
                break;
            case 2:
                listaTmp = Utilidades.inicializarMiradores(getApplicationContext().getFilesDir());
                break;
            case 3:
                listaTmp = Utilidades.inicializarZonasRecreativas(getApplicationContext().getFilesDir());
                break;
            case 4:
                listaTmp = Utilidades.inicializarCasasParque(getApplicationContext().getFilesDir());
                break;
            case 5:
                listaTmp = Utilidades.inicializarCentrosVisitantes(getApplicationContext().getFilesDir());
                break;
            case 6:
                listaTmp = Utilidades.inicializarArbolesSingulares(getApplicationContext().getFilesDir());
                break;
            case 7:
                listaTmp = Utilidades.inicializarZonasAcampada(getApplicationContext().getFilesDir());
                break;
            case 8:
                listaTmp = Utilidades.inicializarCampamentos(getApplicationContext().getFilesDir());
                break;
            case 9:
                listaTmp = Utilidades.inicializarRefugios(getApplicationContext().getFilesDir());
                break;
            case 10:
                listaTmp = Utilidades.inicializarQuioscos(getApplicationContext().getFilesDir());
                break;
            case 11:
                listaTmp = Utilidades.inicializarSendas(getApplicationContext().getFilesDir());
                break;
            default:
                break;
        }
        listaItems = listaTmp;
        listaItems.setEspacioNatural(espacioNatural);
        listaItems.actualizarEnEspacio();

        if (listaItems.getEstanEnEspacio().isEmpty()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(ListaItemsActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
            } else {
                builder = new AlertDialog.Builder(ListaItemsActivity.this);
            }
            builder.setTitle("Vacío")
                    .setMessage("No se han encontrado elementos del tipo seleccionado en este espacio.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        // Cargar RecyclerView
        RecyclerView rv = findViewById(R.id.item_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapterItem adapter = new RVAdapterItem(listaItems.getEstanEnEspacio(), new RVClickListenerEspacio() {
            @Override
            public void onClickItem(int position) {
                ItemActivity.lista = listaItems;
                Intent myIntent = new Intent(ListaItemsActivity.this, ItemActivity.class);
                myIntent.putExtra("tipo", String.valueOf(posicion));
                myIntent.putExtra("posicion", String.valueOf(position));
                startActivity(myIntent);
            }
        });
        rv.setAdapter(adapter);
    }


}
