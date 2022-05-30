package com.example.reg.Usuario

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
import com.example.reg.Actividades.MainActivity
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.FragmentPerfilBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Perfil : Fragment() {
    val usu by lazy {
        activity as MainActivity
    }
    val SM by lazy {
        SharedManager(usu.contexto)
    }
    var options = RequestOptions ()
        .fallback(R.drawable.nregistrado)
        .error(R.drawable.nregistrado)
        .circleCrop()
                          //FragmentNombrefragmento
    private var _binding: FragmentPerfilBinding? = null
    var url_imagen: Uri?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.estadisticaFactura)
    }

    override fun onStart() {
        super.onStart()
        Glide.with(usu.contexto).load(usu.usuarioActual.imagen).apply(options).into(binding.usuEImage)
        binding.usuENombre.setText(usu.usuarioActual.nombre.toString())
        binding.usuEImage.setOnClickListener {
            obtener_url.launch("image/*")
        }
    }

    private val obtener_url= registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri?->
        when (uri){
            null-> Snackbar.make(binding.usuENombre, "Ninguna imagen selecionada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            else->{
                url_imagen=uri
                Glide.with(usu.contexto).load(url_imagen).apply(options).into(binding.usuEImage)
                Snackbar.make(binding.usuENombre, "Imagen selecionada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    fun actualizarUsu(v:View){
        if(isValid()){
            usu.binding.fab.hide()
            GlobalScope.launch(Dispatchers.IO) {
                val nombre=binding.usuENombre.text.toString().trim()
                val password=if(binding.usuEContrasena.text!!.isEmpty()){
                    usu.usuarioActual!!.password.toString()
                }else{
                    binding.usuEContrasena!!.text!!.trim().toString()
                }
                val url_imagen_firebase=if(url_imagen!=null){
                    insertoImagen(generoId(inmobiliaria, usuariosBD).toString(), url_imagen!!)
                }else{
                    usu.usuarioActual!!.imagen.toString()
                }

                usu.runOnUiThread { Snackbar.make(binding.usuENombre, "Usuario Actualizado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show() }
                val renovado=Usuario(SM.idUsuario,usu.usuarioActual!!.correo,nombre,password,0,url_imagen_firebase,true)
                usu.insertoUsu(SM.idUsuario,usu.usuarioActual!!.correo.toString(),nombre,password,0,url_imagen_firebase,true)
                usu.runOnUiThread {
                    usu.navController.navigate(R.id.navigation_principal)
                    usu.usuarioActual=renovado
                }
            }
        }
    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.usuENombre, this::validoCampo)
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

    override fun onResume() {
        super.onResume()
        usu.FAB_manager(4,this::actualizarUsu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}