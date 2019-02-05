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
import android.widget.TextView;

import ru.noties.markwon.Markwon;

public class TextoActivity extends AppCompatActivity {
    private TextView texto;
    private String accion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accion = getIntent().getStringExtra("accion");
        texto = findViewById(R.id.texto);

        switch (accion) {
            case "politica_privacidad":
                Markwon.setMarkdown(texto, getString(R.string.politica_privacidad_texto));
                setTitle(getString(R.string.politica_privacidad));
                break;

            case "licencia_tercero":
                Markwon.setMarkdown(texto, getString(R.string.licencia_terceros));
                setTitle(getString(R.string.terceros));
                break;
            case "conjunto_datos":
                Markwon.setMarkdown(texto, getString(R.string.datos));
                setTitle(getString(R.string.conjunto_datos));
                break;
            case "leyenda":
                Markwon.setMarkdown(texto, getString(R.string.leyenda));
                setTitle(getString(R.string.informacion));
                break;
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }*/

    /*@Override
    public void onBackPressed() {
        onKeyDown(0,null);
    }*/

    private void atras() {
        switch (accion) {
            default:
                startActivity(new Intent(TextoActivity.this, MainActivity.class));
                break;
            case "licencia_app":
                startActivity(new Intent(TextoActivity.this, AcercaDeActivity.class));
                break;
            case "leyenda":
                startActivity(new Intent(TextoActivity.this, EspacioActivity.class));
                break;
        }
    }
}

