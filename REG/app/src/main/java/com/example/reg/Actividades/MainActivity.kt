package com.example.reg.Actividades

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reg.*
import com.example.reg.AdaptadoresRecycler.AdaptadorMensajes
import com.example.reg.Objetos.Mensaje
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    private lateinit var binding: ActivityMainBinding
    lateinit var usuarioActual:Usuario
    lateinit var emisor:Usuario
    lateinit var receptor:Usuario
    val SM by lazy {
        SharedManager(this)
    }

    val listaMensajes by lazy {
        añadoListaMensajes()
    }

    val adaptadorListaMensajes by lazy {
        AdaptadorMensajes(listaMensajes,this,emisor,receptor)
    }
    var contexto=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioActual= Usuario()
        //PARA RECIBIRLO
        val bundle = intent.extras
        usuarioActual = bundle?.getParcelable<Usuario>("UsuarioActual")?:Usuario()
        emisor=usuarioActual

        GlobalScope.launch(Dispatchers.IO){
            receptor= sacoUsuarioDeLaBase("jose@gmail.com")
        }

        val navView: BottomNavigationView = binding.navView
        FAB_manager(0){}

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_incidencias, R.id.navigation_facturas,R.id.navigation_principal,R.id.navigation_perfil,R.id.navigation_chatPlantilla
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

    }


    fun añadoListaMensajes():MutableList<Mensaje>{
        val lista= mutableListOf<Mensaje>()

        db_ref.child(inmobiliaria).child(chatBD)
            .child(mensajeBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val mess=hijo?.getValue(Mensaje::class.java)
                        if(mess!!.usu_emisor==emisor.id && mess!!.usu_receptor==receptor.id
                            || mess!!.usu_emisor==receptor.id && mess!!.usu_receptor==emisor.id){
                            lista.add(mess)
                        }
                        adaptadorListaMensajes.notifyDataSetChanged()
                    }

                    //adaptadorListaMensajes.notifyItemChanged(listaMensajes.size)

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
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
                    setOnClickListener { view ->
                        navController.navigate(R.id.navigation_chatPlantilla)
                    }

                }


            }

        }
    }


}