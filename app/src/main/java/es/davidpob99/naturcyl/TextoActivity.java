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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.noties.markwon.Markwon;

public class TextoActivity extends AppCompatActivity {
    private String accion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accion = getIntent().getStringExtra("accion");
        TextView texto = findViewById(R.id.texto);

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

