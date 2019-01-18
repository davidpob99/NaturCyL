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

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RVAdapterEspacioItem extends RecyclerView.Adapter<RVAdapterEspacioItem.EspacioItemViewHolder> {
    private ArrayList<EItem> items;
    private RVClickListenerEspacio listener;

    public RVAdapterEspacioItem(ArrayList<EItem> items, RVClickListenerEspacio listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RVAdapterEspacioItem.EspacioItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.espacio_item_cardview, viewGroup, false);
        final RVAdapterEspacioItem.EspacioItemViewHolder pvh = new RVAdapterEspacioItem.EspacioItemViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(v, pvh.getLayoutPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(EspacioItemViewHolder espacioItemViewHolder, int i) {
        espacioItemViewHolder.itemNombre.setText(items.get(i).getNombre());
        espacioItemViewHolder.itemFoto.setImageResource(items.get(i).getFoto());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class EspacioItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemNombre;
        ImageView itemFoto;

        EspacioItemViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.espacio_item_cv);
            itemNombre = v.findViewById(R.id.espacio_item_nombre);
            itemFoto = v.findViewById(R.id.espacio_item_foto);
        }
    }
}