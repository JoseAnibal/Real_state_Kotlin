package com.example.reg.Actividades

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.AdaptadoresRecycler.*
import com.example.reg.Notificaciones.Notificacion
import com.example.reg.Notificaciones.Notificaciones
import com.example.reg.Objetos.*
import com.example.reg.databinding.ActivityAdminBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class Admin : AppCompatActivity(), OnMapReadyCallback {

    val noti by lazy {
        Notificaciones(this)
    }

    lateinit var map:GoogleMap

    var coordsPiso= mutableListOf<String>()
    var idPiso=""
    var idUsu=""
    var facturilla=Factura()
    var incidencia=Incidencia()

    val SM by lazy {
        SharedManager(this)
    }
    //PISOS
    val listaPisos by lazy {
        añadoListaPisos()
    }

    val adaptadorListaPisos by lazy {
        AdaptadorPisos(listaPisos,this)
    }

    //USUARIOS REGISTRADOS
    val listaUsuarios by lazy {
        añadoListaUsuarios()
    }

    val adaptadorListaUsuarios by lazy {
        AdaptadorUsuariosLista(listaUsuarios,this,0)
    }

    //USUARIOS NO REGISTRADOS
    val listaUsuNoRegistrados by lazy {
        añadoListaUsuNoregistrados()
    }

    val adaptadorListaUsuNoRegistrados by lazy {
        AdaptadorUsuariosNoRegistrados(listaUsuNoRegistrados,this)
    }

    //USUARIOS ASIGNACION
    val listaUsuariosAsignacion by lazy {
        añadoListaIDUsuSinPisos()
    }

    val adaptadorListaUsuarioAsignacion by lazy {
        AdaptadorAsignacion(listaUsuariosAsignacion,this)
    }

    //INCIDENCIAS
    val listadeIncidencias by lazy{
        añadoListaIncidencias()
    }

    val adaptadorListaIncidencias by lazy {
        AdaptadorIncidencias(listadeIncidencias,this,0)
    }
    //CHATS
    val adaptadorSalasUsuarios by lazy {
        AdaptadorChatsUsuarios(listaUsuarios,this)
    }

    val adaptadorListaMensajes by lazy {
        AdaptadorMensajesAdmin(mutableListOf(),this)
    }

    val contexto by lazy {
        this
    }

    var spchat=Usuario()
    var usuarioChat=Usuario()

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityAdminBinding
    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notisListener()
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noti.createNotificationChannel()
        setSupportActionBar(binding.appBarAdmin.toolbar)
        FAB_manager(1){}
        GlobalScope.launch(Dispatchers.IO){
            spchat= sacoUsuarioDeLaBase("jose@gmail.com")
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_admin)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_pisos,R.id.nav_usuarios,R.id.nav_adminPendientes,R.id.nav_adminIncidencias,R.id.nav_chatsUsuarios
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

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
                    adaptadorListaIncidencias.filter.filter(p0)
                    adaptadorSalasUsuarios.filter.filter(p0)
                    adaptadorListaUsuarioAsignacion.filter.filter(p0)
                    adaptadorListaUsuarios.filter.filter(p0)
                    adaptadorListaUsuNoRegistrados.filter.filter(p0)
                    adaptadorListaPisos.filter.filter(p0)
                    return false
                }
            })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.cerrarSesion ->{
                SM.idUsuario=getString(R.string.idUsuarioDef)
                redireccionar(this,LoggedUser())
                true
            }

            R.id.modoOscuroAdmin ->{
                if(contexto.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)== Configuration.UI_MODE_NIGHT_YES){
                    SM.modoOscuro=false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }else{
                    SM.modoOscuro=true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                true
            }

            R.id.eliminarTodaRelacion ->{
                eliminoListaIncidenciasSinPiso()
                eliminoListaFacturasSinPiso()
                Snackbar.make(binding.appBarAdmin.fab, "Toda relacion con piso eliminada (Excepto Usuarios)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    fun añadoListaUsuarios():MutableList<Usuario>{
        val lista= mutableListOf<Usuario>()

        db_ref.child(inmobiliaria)
            .child(usuariosBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(Usuario::class.java)
                        if (ussu != null && ussu.tipo==0 && ussu.resgistrado!!) {
                            lista.add(ussu)
                        }
                    }
                    adaptadorListaUsuarios.notifyItemChanged(listaUsuarios.size)
                    adaptadorListaUsuarios.notifyDataSetChanged()
                    adaptadorSalasUsuarios.notifyItemChanged(listaUsuarios.size)
                    adaptadorSalasUsuarios.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
    }

    fun añadoListaIDUsuSinPisos():MutableList<Usuario>{
        val lista= mutableListOf<String>()
        val listadevolver= mutableListOf<Usuario>()
        db_ref.child(inmobiliaria)
            .child(UsuarioPisoBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    listadevolver.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(UsuarioPiso::class.java)
                        if (ussu != null) {
                            ussu.idUsuario?.let { lista.add(it) }
                        }
                    }

                    adaptadorListaUsuarios.lista.forEachIndexed { i, u ->
                        if(!lista.contains(u.id)){
                            listadevolver.add(u)
                        }
                    }
                    adaptadorListaUsuarioAsignacion.notifyItemChanged(listaUsuariosAsignacion.size)
                    adaptadorListaUsuarioAsignacion.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return listadevolver
    }

    fun añadoListaUsuNoregistrados():MutableList<Usuario>{
        val lista= mutableListOf<Usuario>()

        db_ref.child(inmobiliaria)
            .child(usuariosBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(Usuario::class.java)
                        if (ussu != null && ussu.tipo==0 && !ussu.resgistrado!!) {
                            lista.add(ussu)
                        }
                    }

                    adaptadorListaUsuNoRegistrados.notifyItemChanged(listaUsuNoRegistrados.size)
                    adaptadorListaUsuNoRegistrados.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
    }

    fun añadoListaIncidencias():MutableList<Incidencia>{
        val lista= mutableListOf<Incidencia>()

        db_ref.child(inmobiliaria).child(incidenciaBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val incci=hijo?.getValue(Incidencia::class.java)
                        if(incci!=null){
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

    fun añadoListaMensajes(idusu:String):MutableList<Mensaje>{
        val lista= mutableListOf<Mensaje>()

        db_ref.child(inmobiliaria).child(chatBD)
            .child(mensajeBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val mess=hijo?.getValue(Mensaje::class.java)
                        if((idusu==mess?.usu_receptor && spchat.id==mess.usu_emisor) ||
                            (idusu==mess?.usu_emisor && spchat.id==mess.usu_receptor)){
                            lista.add(mess)
                        }
                        adaptadorListaMensajes.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
    }

    fun notisListener(){
        db_ref.child(inmobiliaria)
            .child(notificaionesBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val notti=hijo?.getValue(Notificacion::class.java)
                        if (notti != null && notti.idUsuario==SM.idUsuario) {
                            if(notti.tipo==0){
                                noti.crearNotificacionIncidencia(notti.titulo.toString(),notti.descripcion.toString())
                            }else{
                                noti.crearNotificacionParaAdmint(notti.titulo.toString(),notti.descripcion.toString())
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
    }

    fun eliminoListaFacturasSinPiso(){
        db_ref.child(inmobiliaria)
            .child(facturaBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(Factura::class.java)
                        if (ussu != null && ussu.idPiso==idPiso) {
                            db_ref.child(inmobiliaria).child(facturaBD).child(ussu.id!!).removeValue()
                        }
                        db_ref.child(inmobiliaria)
                            .child(facturaBD).removeEventListener(this)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
    }

    fun eliminoListaIncidenciasSinPiso(){
        db_ref.child(inmobiliaria)
            .child(incidenciaBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(Incidencia::class.java)
                        if (ussu != null && ussu.idPiso==idPiso) {
                            db_ref.child(inmobiliaria).child(incidenciaBD).child(ussu.id!!).removeValue()
                        }
                        db_ref.child(inmobiliaria)
                            .child(incidenciaBD).removeEventListener(this)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
    }

    fun FAB_manager(mode:Int, listener:(View)->Unit){
        when(mode){
            1 -> {
                //REDIRECCIONAR A AÑADIR PISOS
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_add_24)
                    setOnClickListener {
                        navController.navigate(R.id.nav_adminAddPiso)
                    }
                }
            }
            2 -> {
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_done_all_24)
                    //PISOSAÑADIR
                    setOnClickListener(listener)
                }
            }

            3 -> {
                //REDIRECCIONAR A EDITAR PISO
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_edit_24)
                    setOnClickListener { view ->
                        navController.navigate(R.id.nav_editarPiso)
                    }
                }
            }

            4 -> {
                //GUARDAR PISO
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_save_24)
                    //PISOSAÑADIR
                    setOnClickListener(listener)
                }
            }

            5 -> {
                //NO HACE NA, DESAPARECE
                binding.appBarAdmin.fab.hide()
            }

            6 -> {
                //GUARDAR USU
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_save_24)
                    setOnClickListener(listener)
                }
            }

            7 -> {
                //REDIRECCIONAR A AÑADIR FACTURA
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_add_24)
                    setOnClickListener { view ->
                        navController.navigate(R.id.nav_adminAddFactura)
                    }
                }
            }

            8 -> {
                //GUARDAR FACTURA
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_save_24)
                    //Facturaañadir
                    setOnClickListener(listener)
                }
            }

            9 -> {
                //INCIDENCIA
                binding.appBarAdmin.fab.show()
                (binding.appBarAdmin.fab).apply{
                    setImageResource(R.drawable.ic_baseline_save_24)
                    //Facturaañadir
                    setOnClickListener(listener)
                }
            }

        }
    }

    fun insertoPiso(id:String,calle:String,imagenes:MutableList<String>,nhabs:String,nbath:String,m2:String,desc:String,estado:Boolean,precio:Int,coords:String,codPostal:String){
        val creoPiso=Piso(id, calle,imagenes, nhabs,nbath,m2,desc,estado,precio,coords,codPostal)
        db_ref.child(inmobiliaria).child(pisosBD).child(id).setValue(creoPiso)

    }

    fun insertoUsu(id:String,correo:String,nombre:String,password:String,tipo:Int,imagen:String,registrado:Boolean){
        val creoUsu=Usuario(id, correo, nombre, password, tipo, imagen, registrado)
        db_ref.child(inmobiliaria).child(usuariosBD).child(id).setValue(creoUsu)
    }

    fun usuarioPisoCrear(id:String,idUsuario:String,idPiso:String){
        val usuPiso=UsuarioPiso(id,idUsuario,idPiso)
        db_ref.child(inmobiliaria).child(UsuarioPisoBD).child(id).setValue(usuPiso)
    }

    fun insertoFactura(id:String,idPiso:String,luz:String,agua:String,internet:String,gastosExtra:String,fecha:String,total:String){
        val fact=Factura(id,idPiso,luz,agua,internet,gastosExtra,fecha,total)
        db_ref.child(inmobiliaria).child(facturaBD).child(id).setValue(fact)
    }

    fun insertarIncidencia(id:String,idPiso:String,titulo:String,desc:String,estado:Int,imagenInci:String,fecha:String){
        val inci=Incidencia(id,idPiso,titulo,desc,estado,imagenInci,fecha)
        db_ref.child(inmobiliaria).child(incidenciaBD).child(id).setValue(inci)
    }

    fun insertarNotificacion(id:String,tipo:Int,titulo:String,desc:String,idUsuario:String){
        val noti=Notificacion(id,tipo,titulo,desc,idUsuario)
        db_ref.child(inmobiliaria).child(notificaionesBD).child(id).setValue(noti)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        createMarker()
    }

    fun createMarker(){
        val coordinates=LatLng(coordsPiso[0].toDouble(),coordsPiso[1].toDouble())
        val marker=MarkerOptions().position(coordinates).title("Ubicacion del piso")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            1000,
            null
        )
    }
}