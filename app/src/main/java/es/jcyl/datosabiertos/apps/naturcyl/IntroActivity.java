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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatusBar(false);
        setSkipText("SALTAR");
        setDoneText("HECHO");

        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Almacenamiento",
                "La aplicación utiliza el almacenamiento para descargar los datos y los mapas. Si no permites su uso, la app se cerrará.",
                R.drawable.ic_folder_black_24dp,
                getResources().getColor(R.color.colorPrimary)));

        /*addSlide(AppIntroFragment.newInstance("Información",
                "Para guardar una nueva jugada o lance, pulse en el botón de las cartas (abajo a la derecha).",
                R.drawable.screenshot_info,
                getResources().getColor(R.color.colorPrimaryDark)));

        addSlide(AppIntroFragment.newInstance("Bienvenido",
                "Rellene los datos correspondientes a cada jugada y después de haber descubierto las cartas marque el ganador de cada una y pulse el botón.",
                R.drawable.screenshot_mano,
                getResources().getColor(R.color.colorAccent)));*/


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
