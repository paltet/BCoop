package com.bcoop.bcoop

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
                backgroundDrawable = getPhoto(1)

                ))

        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial3),
                description = getString(R.string.tutorial3_2),
                backgroundDrawable = getPhoto(3)
        ))

        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial4),
                //description = getString(R.string.tutorial4_2),
                backgroundDrawable = getPhoto(4)

        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial5),
                //description = getString(R.string.tutorial5_2),
                backgroundDrawable = getPhoto(5)
        ))
        addSlide(AppIntroFragment.newInstance(
                //title = getString(R.string.tutorial6),
                //description = getString(R.string.tutorial6_2),
                backgroundDrawable = getPhoto(6)
        ))
        addSlide(AppIntroFragment.newInstance(
                //title = getString(R.string.tutorial7),
                description = getString(R.string.tutorial7_2),
                backgroundDrawable = getPhoto(7)

        ))
        addSlide(AppIntroFragment.newInstance(
                //title = getString(R.string.tutorial8),
                description = getString(R.string.tutorial8_2),
                backgroundDrawable = getPhoto(9)
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial9),
                //description = getString(R.string.tutorial9_2),
                backgroundDrawable = getPhoto(8)
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

    private fun getPhoto(num: Int): Int {
        val lang = getResources().getText(R.string.language)
        var photo = 0

        when(num){
            1 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto1
                    "cast" -> photo = R.drawable.foto1_cast
                    "eng" -> photo = R.drawable.foto1_eng
                }
            }
            3-> {
                when(lang){
                    "cat" -> photo = R.drawable.foto3
                    "cast" -> photo = R.drawable.foto3_cast
                    "eng" -> photo = R.drawable.foto3_eng
                }
            }
            4 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto4
                    "cast" -> photo = R.drawable.foto4_cast
                    "eng" -> photo = R.drawable.foto4_eng
                }

            }
            5 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto5
                    "cast" -> photo = R.drawable.foto5_cast
                    "eng" -> photo = R.drawable.foto5_eng
                }
            }
            6 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto6
                    "cast" -> photo = R.drawable.foto6_cast
                    "eng" -> photo = R.drawable.foto6_eng
                }
            }
            7 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto7
                    "cast" -> photo = R.drawable.foto7_cast
                    "eng" -> photo = R.drawable.foto7_eng
                }
            }
            8 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto8
                    "cast" -> photo = R.drawable.foto8_cast
                    "eng" -> photo = R.drawable.foto8_eng
                }
            }
            9 -> {
                when(lang){
                    "cat" -> photo = R.drawable.foto9
                    "cast" -> photo = R.drawable.foto9_cast
                    "eng" -> photo = R.drawable.foto9_eng
                }
            }

        }

        return photo
    }


}
