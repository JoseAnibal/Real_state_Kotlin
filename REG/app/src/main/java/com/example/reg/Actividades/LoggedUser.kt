package com.example.reg.Actividades

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.reg.*
import com.example.reg.AdaptadoresRecycler.AdaptadorPisos
import com.example.reg.Invitado.TabbedActivity.SectionsPagerAdapter
import com.example.reg.Objetos.Piso
import com.example.reg.databinding.ActivityLoggedUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LoggedUser : AppCompatActivity() {

    val listaPisos by lazy {
        añadoListaPisos()
    }

    val adaptadorListaPisos by lazy {
        AdaptadorPisos(listaPisos,this)
    }

    val contexto by lazy {
        this
    }

    val SM by lazy {
        SharedManager(this)
    }

    private lateinit var binding: ActivityLoggedUserBinding

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin, menu)
        (menu.findItem(R.id.busqueda).actionView as SearchView)
            .setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adaptadorListaPisos.filter.filter(p0)
                    return false
                }
            })
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        val intent: Intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        SM.idUsuario=getString(R.string.idUsuarioDef)
    }

    fun añadoListaPisos():MutableList<Piso>{
        val lista= mutableListOf<Piso>()

        db_ref.child(inmobiliaria)
            .child(pisosBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val pisso=hijo?.getValue(Piso::class.java)
                        if (pisso != null) {
                            lista.add(pisso)
                        }
                    }
                    adaptadorListaPisos.notifyItemChanged(listaPisos.size)
                    adaptadorListaPisos.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
    }


}