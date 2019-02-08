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

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RVAdapterItem extends RecyclerView.Adapter<RVAdapterItem.ItemViewHolder> {
    private final ArrayList<? extends EspacioNaturalItem> items;
    private final RVClickListenerEspacio listener;

    public RVAdapterItem(ArrayList<? extends EspacioNaturalItem> items, RVClickListenerEspacio listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RVAdapterItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);
        final RVAdapterItem.ItemViewHolder pvh = new RVAdapterItem.ItemViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(pvh.getLayoutPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterItem.ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.itemNombre.setText(items.get(i).getNombre());
        try {
            itemViewHolder.itemEstado.setText("Estado: " + EspacioNaturalItem.estados[items.get(i).getEstado() - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            itemViewHolder.itemEstado.setText("Estado: Sin determinar");
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        final CardView cv;
        final TextView itemNombre;
        final TextView itemEstado;

        ItemViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.item_cv);
            itemNombre = v.findViewById(R.id.item_nombre);
            itemEstado = v.findViewById(R.id.item_estado);
        }
    }
}