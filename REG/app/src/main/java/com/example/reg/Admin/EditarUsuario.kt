package com.example.reg.Admin

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.Objetos.UsuarioPiso
import com.example.reg.databinding.FragmentEditarUsuarioBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditarUsuario : Fragment() {
    val admin by lazy {
        activity as Admin
    }
    val usuario by lazy {
        admin.listaUsuarios.find { it.id==admin.idUsu }
    }
    var url_imagen: Uri?=null

    var options = RequestOptions ()
        .fallback(R.drawable.nregistrado)
        .error(R.drawable.nregistrado)
        .circleCrop()
                          //FragmentNombrefragmento
    private var _binding: FragmentEditarUsuarioBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(6,this::actualizarUsu)
    }

    override fun onStart() {
        super.onStart()

        Glide.with(admin.contexto).load(usuario!!.imagen).apply(options).into(binding.editImage)
        binding.editNombre.setText(usuario!!.nombre.toString())
        binding.editCorreo.setText(usuario!!.correo.toString())

        binding.editImage.setOnClickListener {
            obtener_url.launch("image/*")
        }

        binding.editEliminar.setOnClickListener {
            eliminoListaUsuariosSinPiso()
            db_ref.child(inmobiliaria).child(usuariosBD).child(admin.idUsu).removeValue()
            admin.navController.navigate(R.id.nav_usuarios)
            Snackbar.make(it, "Usuario eliminado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    private val obtener_url= registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri?->
        when (uri){
            null-> Snackbar.make(binding.editNombre, "Ninguna imagen selecionada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            else->{
                url_imagen=uri
                Glide.with(admin.contexto).load(url_imagen).apply(options).into(binding.editImage)
                Snackbar.make(binding.editNombre, "Imagen selecionada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    fun actualizarUsu(v:View){
        if(isValid()){
            admin.binding.appBarAdmin.fab.hide()
            GlobalScope.launch(Dispatchers.IO) {
                val nombre=binding.editNombre.text.toString().trim()
                val correo=binding.editCorreo.text.toString().trim()
                val password=if(binding.editContraseA.text!!.isEmpty()){
                    usuario!!.password.toString()
                }else{
                    binding.editContraseA!!.text!!.trim().toString()
                }
                val url_imagen_firebase=if(url_imagen!=null){
                    insertoImagen(generoId(inmobiliaria, usuariosBD).toString(), url_imagen!!)
                }else{
                    usuario!!.imagen.toString()
                }

                admin.runOnUiThread { Snackbar.make(binding.editNombre, "Usuario Actualizado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show() }

                admin.insertoUsu(usuario!!.id!!,correo,nombre,password,0,url_imagen_firebase,true)
                admin.runOnUiThread { admin.navController.navigate(R.id.nav_usuarios)}
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.eliminarTodaRelacion)
    }

    fun eliminoListaUsuariosSinPiso(){
        db_ref.child(inmobiliaria)
            .child(UsuarioPisoBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ussu=hijo?.getValue(UsuarioPiso::class.java)
                        if (ussu != null && ussu.idUsuario==usuario!!.id) {
                            db_ref.child(inmobiliaria).child(UsuarioPisoBD).child(ussu.id!!).removeValue()
                        }
                        db_ref.child(inmobiliaria)
                            .child(UsuarioPisoBD).removeEventListener(this)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.editNombre, this::validoCampo),
            Pair(binding.editCorreo, this::validoCorreo),
        )
        for(c in checkers){
            val x = c.first
            val f = c.second
            val y = f(x)
            validado = y
            if(!validado) break
        }
        return validado
    }

    suspend fun insertoImagen(id:String,foto: Uri):String{

        lateinit var urlImagenFirebase: Uri

        urlImagenFirebase= sto_ref.child(inmobiliaria).child(usuariosBD).child(id)
            .putFile(foto).await().storage.downloadUrl.await()

        return urlImagenFirebase.toString()

    }

    fun validoCorreo(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(v.text.trim()).matches()){
            validado=false
            v.error=getString(R.string.valid_correo)
        }

        return validado
    }

    fun validoCampo(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty()){
            validado=false
            v.error=getString(R.string.campo_vacio)
        }

        return validado
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}