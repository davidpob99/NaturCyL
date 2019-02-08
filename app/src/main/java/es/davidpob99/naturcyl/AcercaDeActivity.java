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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0-standalone.html"));
        startActivity(browserIntent);
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
