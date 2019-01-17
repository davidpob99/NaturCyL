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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adaptador del RecyclerView que contiene los Espacios Naturales y que se encuentra en MainActivity
 */
public class RVAdapterEspacio extends RecyclerView.Adapter<RVAdapterEspacio.EspacioViewHolder> {
    private ArrayList<EspacioNatural> listaEspacios;
    private RVClickListenerEspacio listener;

    public RVAdapterEspacio(ArrayList<EspacioNatural> listaEspacios, RVClickListenerEspacio listener) {
        this.listaEspacios = listaEspacios;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return listaEspacios.size();
    }

    @Override
    public EspacioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.espacio_cardview, viewGroup, false);
        final EspacioViewHolder pvh = new EspacioViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(v, pvh.getLayoutPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(EspacioViewHolder espacioViewHolder, int i) {
        espacioViewHolder.espacioNombre.setText(listaEspacios.get(i).getNombre());
        espacioViewHolder.espacioTipo.setText(listaEspacios.get(i).getTipoDeclaracion());
        Picasso.get().load(EspacioNatural.URL_IMG_BASE + listaEspacios.get(i).getImagen()).into(espacioViewHolder.espacioFoto);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class EspacioViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView espacioNombre;
        TextView espacioTipo;
        ImageView espacioFoto;

        EspacioViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.espacio_cv);
            espacioNombre = v.findViewById(R.id.espacio_item_nombre);
            espacioTipo = v.findViewById(R.id.espacio_item_descripcion);
            espacioFoto = v.findViewById(R.id.espacio_foto);
        }
    }
}
