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
                title = getString(R.string.tutorial1),
                description = getString(R.string.tutorial1_2),
                backgroundDrawable = R.drawable.foto1

                ))

        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial3),
                description = getString(R.string.tutorial3_2),
                backgroundDrawable = R.drawable.foto3
        ))

        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial4),
                //description = getString(R.string.tutorial4_2),
                backgroundDrawable = R.drawable.foto4

        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial5),
                //description = getString(R.string.tutorial5_2),
                backgroundDrawable = R.drawable.foto5
        ))
        addSlide(AppIntroFragment.newInstance(
                //title = getString(R.string.tutorial6),
                //description = getString(R.string.tutorial6_2),
                backgroundDrawable = R.drawable.foto6
        ))
        addSlide(AppIntroFragment.newInstance(
                //title = getString(R.string.tutorial7),
                description = getString(R.string.tutorial7_2),
                backgroundDrawable = R.drawable.foto7

        ))
        addSlide(AppIntroFragment.newInstance(
                //title = getString(R.string.tutorial8),
                description = getString(R.string.tutorial8_2),
                backgroundDrawable = R.drawable.foto9
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial9),
                //description = getString(R.string.tutorial9_2),
                backgroundDrawable = R.drawable.foto8
        ))


    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
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
