package com.bcoop.bcoop

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType


class TutorialActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment

        setTransformer(AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 1.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 2.0
        ))

        addSlide(AppIntroFragment.newInstance(
                title = "Cerca Habilitats",
                description = "Busca les habilitats que més t'interessen",
                backgroundDrawable = R.drawable.foto1

                ))
        addSlide(AppIntroFragment.newInstance(
                title = "Tria l'habilitat",
                description = "Tria una entre les moltes habilitats disponibles",
                backgroundDrawable = R.drawable.foto2
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Visita el perfil",
                description = "Mira el perfil de l'usuari que més t'interessa",
                backgroundDrawable = R.drawable.foto3
        ))

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        //val intent = Intent(this, HomeActivity::class.java)
        //startActivity(intent)
        //finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        //startActivity(new Intent(InitConfigLocationActivity.this, HomeActivity.class));

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

    }
}
