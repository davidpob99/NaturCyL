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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
    }

    public void clickedGit(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/davidpob99/NaturCyl"));
        startActivity(browserIntent);
    }

    public void clickedLicense(View v) {
        try {
            Intent myIntent = new Intent(AcercaDeActivity.this, TextoActivity.class);
            myIntent.putExtra("accion", "licencia_app");
            startActivity(myIntent);
        } catch (Exception e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0-standalone.html"));
            startActivity(browserIntent);
        }
    }

    public void clickedTerceros(View v) {
        Intent myIntent = new Intent(AcercaDeActivity.this, TextoActivity.class);
        myIntent.putExtra("accion", "licencia_tercero");
        startActivity(myIntent);
    }

    public void clickedPrivacidad(View v) {
        Intent myIntent = new Intent(AcercaDeActivity.this, TextoActivity.class);
        myIntent.putExtra("accion", "politica_privacidad");
        startActivity(myIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startActivity(new Intent(AcercaDeActivity.this, MainActivity.class));
        return super.onKeyDown(keyCode, event);
    }
}
