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

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RVAdapterItem extends RecyclerView.Adapter<RVAdapterItem.ItemViewHolder> {
    private ArrayList<? extends EspacioNaturalItem> items;
    private RVClickListenerEspacio listener;

    public RVAdapterItem(ArrayList<? extends EspacioNaturalItem> items, RVClickListenerEspacio listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RVAdapterItem.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);
        final RVAdapterItem.ItemViewHolder pvh = new RVAdapterItem.ItemViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(v, pvh.getLayoutPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVAdapterItem.ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.itemNombre.setText(items.get(i).getNombre());
        itemViewHolder.itemEstado.setText("Estado: " + EspacioNaturalItem.estados[items.get(i).getEstado() - 1]);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemNombre;
        TextView itemEstado;

        ItemViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.item_cv);
            itemNombre = v.findViewById(R.id.item_nombre);
            itemEstado = v.findViewById(R.id.item_estado);
        }
    }
}