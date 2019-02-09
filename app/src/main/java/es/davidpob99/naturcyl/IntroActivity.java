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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatusBar(false);
        setSkipText("SALTAR");
        setDoneText("HECHO");

        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Bienvenido",
                "Aplicación para acceder a los datos de los espacios naturales de la comunidad autónoma de Castilla y León así como sus equipamientos y posibilidades",
                R.drawable.screenshot_espacios,
                getResources().getColor(R.color.colorAccent)));

        addSlide(AppIntroFragment.newInstance("Favoritos",
                "Guarde los equipamientos (aparcamientos, miradores, casas del parque...) para consultarlos más adelante o planificar su visita",
                R.drawable.screenshot_item,
                getResources().getColor(R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("Cercanos",
                "Acceda a los equipamientos más cercanos dada una distancia de su ubicación actual (es necesario el acceso al GPS de su dispositivo)",
                R.drawable.screenshot_cercanos,
                getResources().getColor(R.color.colorPrimaryDark)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Toast.makeText(IntroActivity.this, "Se están descargando los datos. Por favor no cierre la app aunque parezca bloqueada", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Toast.makeText(IntroActivity.this, "Se están descargando los datos. Por favor no cierre la app aunque parezca bloqueada", Toast.LENGTH_LONG).show();
        finish();
    }
}
