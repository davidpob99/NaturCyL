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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class FavoritosActivity extends AppCompatActivity {
    protected static ArrayList<EspacioNaturalItem> favoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Cargar RecyclerView
        RecyclerView rv = findViewById(R.id.favoritos_rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapterItem adapter = new RVAdapterItem(favoritos, new RVClickListenerEspacio() {
            @Override
            public void onClickItem(View v, int position) {
                ListaEspaciosNaturalesItems items = new ListaEspaciosNaturalesItems(favoritos);
                items.deElementosAEnEspacio();
                ItemActivity.lista = items;
                Intent myIntent = new Intent(FavoritosActivity.this, ItemActivity.class);
                myIntent.putExtra("tipo", String.valueOf(-1));
                myIntent.putExtra("posicion", String.valueOf(position));
                startActivity(myIntent);
            }
        });
        rv.setAdapter(adapter);
    }


}
