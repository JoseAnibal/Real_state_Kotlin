package com.example.reg.Actividades

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reg.*
import com.example.reg.AdaptadoresRecycler.AdaptadorFacturas
import com.example.reg.AdaptadoresRecycler.AdaptadorFacturasUsuario
import com.example.reg.AdaptadoresRecycler.AdaptadorIncidencias
import com.example.reg.AdaptadoresRecycler.AdaptadorMensajes
import com.example.reg.Notificaciones.Notificacion
import com.example.reg.Notificaciones.Notificaciones
import com.example.reg.Objetos.*
import com.example.reg.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
data class estadisticas(val nombre:String,val porcentaje:Double,val color:Int)

class MainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    lateinit var binding: ActivityMainBinding
    lateinit var usuarioActual:Usuario
    lateinit var emisor:Usuario
    lateinit var receptor:Usuario
    var idPiso=""
    var piso=Piso()
    var facturilla= Factura()
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
        AdaptadorIncidencias(listaIncidencias,this,1)
    }
    //FACTURASPISO
    val listaFacturas by lazy {
        añadoListaFacturas()
    }

    val adaptadorListaFacturas by lazy {
        AdaptadorFacturasUsuario(listaFacturas,this)
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
        SM.idUsuario=usuarioActual.id!!
        emisor=usuarioActual

        GlobalScope.launch(Dispatchers.IO){
            receptor= sacoUsuarioDeLaBase("jose@gmail.com")
            idPiso= sacoRelacionPiso(usuarioActual.id!!).idPiso.toString()
            piso= sacoPisoDeLaBase(idPiso)
        }

        val navView: BottomNavigationView = binding.navView
        FAB_manager(0){}

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_incidencias, R.id.navigation_facturas,R.id.navigation_principal,R.id.navigation_perfil,R.id.navigation_chatPlantilla,
                R.id.navigation_usuarioAddIncidencia,R.id.navigation_infoFacturaUsuario,R.id.navigation_estadisticasFactura
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.kebab_menu, menu)
        (menu.findItem(R.id.busqueda).actionView as SearchView)
            .setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.modoOscuro ->{
                if(contexto.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)==Configuration.UI_MODE_NIGHT_YES){
                    SM.modoOscuro=false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }else{
                    SM.modoOscuro=true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                true
            }
            R.id.cerrarSesion ->{
                SM.idUsuario=getString(R.string.idUsuarioDef)
                redireccionar(this,LoggedUser())
                true
            }
            R.id.estadisticaFactura ->{
                navController.navigate(R.id.navigation_estadisticasFactura)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun generoEstadistica():MutableList<estadisticas>{
        val listaPasar= mutableListOf<estadisticas>()

        val listaNombres= listOf("luz","agua","internet")
        val listaColores= listOf(Color.rgb(255, 210, 48),Color.rgb(48, 214, 255),Color.rgb(255, 114, 48))
        val valoresSuma= mutableListOf<Double>()
        val listaPorcentaje= mutableListOf<Double>()

        val totalAgua=listaFacturas.sumOf { it.agua!!.toDouble() }
        val totalLuz=listaFacturas.sumOf { it.luz!!.toDouble() }
        val totalInternet=listaFacturas.sumOf { it.internet!!.toDouble() }
        val total=totalLuz+totalAgua+totalInternet

        valoresSuma.add(totalLuz)
        valoresSuma.add(totalAgua)
        valoresSuma.add(totalInternet)

        listaNombres.forEachIndexed { i, s ->
            listaPorcentaje.add(valoresSuma[i]*100.toDouble()/total)
        }

        listaNombres.forEachIndexed { i, s ->
            listaPasar.add(estadisticas(s,listaPorcentaje[i],listaColores[i]))
        }

        return listaPasar
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

    fun añadoListaFacturas():MutableList<Factura>{
        val lista= mutableListOf<Factura>()

        db_ref.child(inmobiliaria).child(facturaBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ffact=hijo?.getValue(Factura::class.java)
                        if(ffact!=null && ffact.idPiso==idPiso){
                            lista.add(ffact)
                        }
                        adaptadorListaFacturas.notifyDataSetChanged()
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

            4 -> {
                binding.fab.show()
                (binding.fab).apply{
                    setImageResource(R.drawable.ic_baseline_save_24)
                    //ACTUZALIZAR USUARIO
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

    fun insertoUsu(id:String,correo:String,nombre:String,password:String,tipo:Int,imagen:String,registrado:Boolean){
        val creoUsu=Usuario(id, correo, nombre, password, tipo, imagen, registrado)
        db_ref.child(inmobiliaria).child(usuariosBD).child(id).setValue(creoUsu)
    }


}