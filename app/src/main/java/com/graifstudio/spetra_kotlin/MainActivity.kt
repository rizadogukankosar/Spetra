package com.graifstudio.spetra_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.graifstudio.spetra_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim)
        binding.splashImage.startAnimation(animationFadeIn)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, BaseActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}