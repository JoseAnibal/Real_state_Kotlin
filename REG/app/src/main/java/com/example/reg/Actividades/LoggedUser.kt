package com.example.reg.Actividades

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.reg.Invitado.TabbedActivity.SectionsPagerAdapter
import com.example.reg.databinding.ActivityLoggedUserBinding

class LoggedUser : AppCompatActivity() {

    private lateinit var binding: ActivityLoggedUserBinding
    val numero=1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoggedUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

    }

    override fun onStart() {
        super.onStart()

    }


}