package com.example.reg.Actividades

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reg.*
import com.example.reg.ChatPrivado.ChatPrivado
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var usuarioActual:Usuario
    val SM by lazy {
        SharedManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioActual= Usuario()

        val navView: BottomNavigationView = binding.navView
        FAB_manager(0){}

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_incidencias, R.id.navigation_facturas,R.id.navigation_principal,R.id.navigation_perfil
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        //PARA RECIBIRLO
        val bundle = intent.extras
        usuarioActual = bundle?.getParcelable<Usuario>("UsuarioActual")?:Usuario()
        val patata="a"

    }

    fun FAB_manager(mode:Int, listener:(View)->Unit){
        when(mode){
            0 -> {
                //NO HACE NA, DESAPARECE
                binding.fab.hide()
            }

            1 -> {
                //REAPARECE PARA REENVIAR AL CHAT
                binding.fab.apply {
                    setImageResource(R.drawable.ic_baseline_chat_24)
                    show()
                    setOnClickListener {
                        redireccionarConUsu(this@MainActivity,ChatPrivado(),"UsuarioActual",usuarioActual)
                    }
                }


            }

        }
    }


}