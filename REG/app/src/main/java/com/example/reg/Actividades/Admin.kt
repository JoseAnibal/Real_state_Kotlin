package com.example.reg.Actividades

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.reg.AdaptadoresRecycler.AdaptadorPisos
import com.example.reg.Objetos.Piso
import com.example.reg.R
import com.example.reg.databinding.ActivityAdminBinding
import com.example.reg.db_ref
import com.example.reg.inmobiliaria
import com.example.reg.pisosBD
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Admin : AppCompatActivity() {

    val listaPisos by lazy {
        añadoListaPisos()
    }

    val adaptadorListaPisos by lazy {
        AdaptadorPisos(listaPisos,this)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding
    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdmin.toolbar)
        FAB_manager(1){}
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_admin)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_pisos,R.id.nav_usuarios
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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

    fun FAB_manager(mode:Int, listener:(View)->Unit){
        when(mode){
            1 -> {
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_add_24)
                    setOnClickListener { view ->
                        navController.navigate(R.id.nav_adminAddPiso)
                    }
                }
            }
            2 -> {
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_done_all_24)
                    //CARTASAÑADIR
                    setOnClickListener(listener)
                }
            }

        }
    }

    fun insertoPiso(id:String,calle:String,imagenes:MutableList<String>,nHabs:String,nBath:String,m2:Double,estado:Boolean){
        val creoPiso=Piso(id, calle,imagenes, nHabs,nBath,m2, estado)
        db_ref.child(inmobiliaria).child(pisosBD).child(id).setValue(creoPiso)
    }
}