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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListaItemsActivity extends AppCompatActivity {
    protected static EspacioNatural espacioNatural;
    private int posicion;
    private ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListaEspaciosNaturalesItems<? extends EspacioNaturalItem> listaTmp = new ListaEspaciosNaturalesItems<>();
        listaItems = new ListaEspaciosNaturalesItems<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtener de EspacioActivity
        posicion = Integer.valueOf(getIntent().getStringExtra("posicion"));

        setTitle(Utilidades.nombresItems[posicion]);

        switch (posicion) {
            case 0:
                listaTmp = Utilidades.inicializarAparcamientos();
                break;/*
            case 1:
                listaItems = Utilidades.inicializarObservatorios();
                break;
            case 2:
                listaItems = Utilidades.inicializarMiradores();
                break;
            case 3:
                listaItems = Utilidades.inicializarZonasRecreativas();
                break;
            case 4:
                listaItems = Utilidades.inicializarCasasParque();
                break;
            case 5:
                listaItems = Utilidades.inicializarCentrosVisitantes();
                break;
            case 6:
                listaItems = Utilidades.inicializarArbolesSingulares();
                break;
            case 7:
                listaItems = Utilidades.inicializarZonasAcampada();
                break;
            case 8:
                listaItems = Utilidades.inicializarCampamentos();
                break;
            case 9:
                listaItems = Utilidades.inicializarRefugios();
                break;
            case 10:
                listaItems = Utilidades.inicializarQuioscos();
                break;
            case 11:
                listaItems = Utilidades.inicializarInfraestructurasMenores();
                break;
            case 12:
                listaItems = Utilidades.inicializarSendas();
                break;*/
        }
        listaItems = listaTmp;
        listaItems.setEspacioNatural(espacioNatural);
        listaItems.actualizarEnEspacio();

        // Cargar RecyclerView
        RecyclerView rv = findViewById(R.id.item_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapterItem adapter = new RVAdapterItem(listaItems.getEstanEnEspacio(), new RVClickListenerEspacio() {
            @Override
            public void onClickItem(View v, int position) {
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
