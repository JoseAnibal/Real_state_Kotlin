package com.example.reg.Actividades

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
import com.example.reg.AdaptadoresRecycler.AdaptadorIncidencias
import com.example.reg.AdaptadoresRecycler.AdaptadorMensajes
import com.example.reg.Notificaciones.Notificacion
import com.example.reg.Notificaciones.Notificaciones
import com.example.reg.Objetos.Incidencia
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
    lateinit var binding: ActivityMainBinding
    lateinit var usuarioActual:Usuario
    lateinit var emisor:Usuario
    lateinit var receptor:Usuario
    var idPiso=""
    val SM by lazy {
        SharedManager(this)
    }
    //MENSAJES
    val listaMensajes by lazy {
        añadoListaMensajes()
    }

    val adaptadorListaMensajes by lazy {
        AdaptadorMensajes(listaMensajes,this,emisor,receptor)
    }

    //INCIDENCIAS
    val listaIncidencias by lazy {
        añadoListaIncidencias()
    }

    val adaptadorListaIncidencias by lazy {
        AdaptadorIncidencias(listaIncidencias,this)
    }

    val noti by lazy {
        Notificaciones(this)
    }

    var contexto=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioActual= Usuario()
        //PARA RECIBIRLO
        val bundle = intent.extras
        usuarioActual = bundle?.getParcelable("UsuarioActual")?:Usuario()
        emisor=usuarioActual

        GlobalScope.launch(Dispatchers.IO){
            receptor= sacoUsuarioDeLaBase("jose@gmail.com")
            idPiso= sacoRelacionPiso(usuarioActual.id!!).idPiso.toString()
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

    fun añadoListaIncidencias():MutableList<Incidencia>{
        val lista= mutableListOf<Incidencia>()

        db_ref.child(inmobiliaria).child(incidenciaBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val incci=hijo?.getValue(Incidencia::class.java)
                        if(incci!=null && incci.idPiso==idPiso){
                            lista.add(incci)
                        }
                        adaptadorListaIncidencias.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
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

            2 -> {
                //REDIRECCIONAR A AÑADIR INCIDENCIAS
                binding.fab.show()
                (binding.fab).apply{
                    setImageResource(R.drawable.ic_baseline_add_24)
                    show()
                    setOnClickListener { view ->
                        navController.navigate(R.id.navigation_usuarioAddIncidencia)
                    }
                }
            }

            3 -> {
                binding.fab.show()
                (binding.fab).apply{
                    setImageResource(R.drawable.ic_baseline_save_24)
                    //INCIDENCIASAÑADIR
                    setOnClickListener(listener)
                }
            }

        }
    }

    fun insertarIncidencia(id:String,idPiso:String,titulo:String,desc:String,estado:Int,imagenInci:String,fecha:String){
        val inci=Incidencia(id,idPiso,titulo,desc,estado,imagenInci,fecha)
        db_ref.child(inmobiliaria).child(incidenciaBD).child(id).setValue(inci)
    }

    fun insertarNotificacion(id:String,titulo:String,desc:String,idUsuario:String){
        val noti=Notificacion(id,titulo,desc,idUsuario)
        db_ref.child(inmobiliaria).child(notificaionesBD).child(id).setValue(noti)
    }


}