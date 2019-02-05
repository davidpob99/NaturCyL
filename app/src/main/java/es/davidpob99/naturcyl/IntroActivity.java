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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

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
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }
}
