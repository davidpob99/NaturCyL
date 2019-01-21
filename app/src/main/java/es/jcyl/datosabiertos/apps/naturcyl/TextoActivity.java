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
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import ru.noties.markwon.Markwon;

import static android.text.Html.fromHtml;

public class TextoActivity extends AppCompatActivity {
    private TextView texto;
    private String accion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accion = getIntent().getStringExtra("accion");
        texto = findViewById(R.id.texto);

        switch (accion) {
            /*case "politica_privacidad":
                texto.setText(fromHtml(getString(R.string.privacy_text)));
                setTitle(getString(R.string.action_privacy));
                break;*/

            case "licencia_tercero":
                Markwon.setMarkdown(texto, getString(R.string.licencia_terceros));
                setTitle(getString(R.string.terceros));
                break;

            case "licencia_app":
                try {
                    texto.setText(fromHtml(getString(R.string.licencia_app)));
                    setTitle(getString(R.string.gnu_gpl_v3));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Back button
                } catch (Exception e) {
                    Log.e("GNU GPL v3", "exception", e);/*
                    startActivity(new Intent(TextoActivity.this, AboutActivity.class));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0-standalone.html"));
                    startActivity(browserIntent);*/
                }
                break;
            case "conjunto_datos":
                Markwon.setMarkdown(texto, getString(R.string.datos));
                setTitle(getString(R.string.conjunto_datos));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        atras();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        atras();
    }

    private void atras() {
        switch (accion) {
            default:
                startActivity(new Intent(TextoActivity.this, MainActivity.class));
                break;
            case "licencia_app":
                startActivity(new Intent(TextoActivity.this, AcercaDeActivity.class));
                break;
        }
    }
}

