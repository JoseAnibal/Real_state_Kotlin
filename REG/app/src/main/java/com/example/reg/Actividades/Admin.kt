package com.example.reg.Actividades

import android.content.Context
import android.content.Intent
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
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.AdaptadoresRecycler.AdaptadorFotosPiso
import com.example.reg.AdaptadoresRecycler.AdaptadorPisos
import com.example.reg.AdaptadoresRecycler.AdaptadorUsuariosLista
import com.example.reg.AdaptadoresRecycler.AdaptadorUsuariosNoRegistrados
import com.example.reg.Objetos.Piso
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.ActivityAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Admin : AppCompatActivity() {

    val SM by lazy {
        SharedManager(this)
    }

    val listaPisos by lazy {
        añadoListaPisos()
    }

    val adaptadorListaPisos by lazy {
        AdaptadorPisos(listaPisos,this)
    }

    val listaUsuarios by lazy {
        añadoListaUsuarios()
    }

    val adaptadorListaUsuarios by lazy {
        AdaptadorUsuariosLista(listaUsuarios,this)
    }

    val listaUsuNoRegistrados by lazy {
        añadoListaUsuNoregistrados()
    }

    val adaptadorListaUsuNoRegistrados by lazy {
        AdaptadorUsuariosNoRegistrados(listaUsuNoRegistrados,this)
    }

    val contexto by lazy {
        this
    }

    var idPiso=""

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityAdminBinding
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
                R.id.nav_pisos,R.id.nav_usuarios,R.id.nav_adminPendientes
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        if(SM.idUsuario!="admin"){
            redireccionar(this,LoggedUser())
        }
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
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
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

    fun FAB_manager(mode:Int, listener:(View)->Unit){
        when(mode){
            1 -> {
                //REDIRECCIONAR A AÑADIR PISOS
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

        }
    }

    fun insertoPiso(id:String,calle:String,imagenes:MutableList<String>,nhabs:String,nbath:String,m2:Double,desc:String,estado:Boolean){
        val creoPiso=Piso(id, calle,imagenes, nhabs,nbath,m2,desc,estado)
        db_ref.child(inmobiliaria).child(pisosBD).child(id).setValue(creoPiso)
    }
}