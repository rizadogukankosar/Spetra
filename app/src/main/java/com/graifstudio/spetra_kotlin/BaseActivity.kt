package com.graifstudio.spetra_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.graifstudio.spetra_kotlin.adapters.ViewPagerAdapter
import com.graifstudio.spetra_kotlin.databinding.ActivityBaseBinding
import com.graifstudio.spetra_kotlin.fragments.TranslateTextFragment
import com.graifstudio.spetra_kotlin.fragments.TranslateVoiceFragment

class BaseActivity : AppCompatActivity() {
    lateinit var translateTextFragment: TranslateTextFragment
    lateinit var translateVoiceFragment: TranslateVoiceFragment
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        translateTextFragment = TranslateTextFragment()
        translateVoiceFragment = TranslateVoiceFragment()

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(translateVoiceFragment, "Voice")
        adapter.addFragment(translateTextFragment, "Text")
        //Adapter'ımızdaki verileri viewPager adapter'a veriyoruz

        binding.viewPager.adapter = adapter
        //Tablar arasında yani viewPager'lar arasında geçisi sağlıyoruz
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        translateVoiceFragment.onActivityResult(requestCode, resultCode, data)
    }
}