package com.example.reg.Usuario

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.Actividades.MainActivity
import com.example.reg.Notificaciones.Notificaciones
import com.example.reg.databinding.FragmentUsuarioAddIncidenciaBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class UsuarioAddIncidencia : Fragment() {
    val usuario by lazy {
        activity as MainActivity
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentUsuarioAddIncidenciaBinding? = null
    var url_imagen: Uri?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    var options = RequestOptions ()
        .fallback(R.drawable.nregistrado)
        .error(R.drawable.nregistrado)
        .fitCenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentUsuarioAddIncidenciaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.incImagen.setOnClickListener {
            obtener_url.launch("image/*")
        }
    }

    override fun onResume() {
        super.onResume()
        usuario.FAB_manager(3,this::insertarIncidencia)
    }

    fun insertarIncidencia(v:View){
        if(isValid()){
            usuario.binding.fab.hide()
            GlobalScope.launch(Dispatchers.IO) {
                val id= generoId(inmobiliaria, incidenciaBD)
                val titulo=binding.incTitulo.text.toString()
                val desc=binding.incDesc.text.toString()
                val fecha=obtenerFechaActual().split(" ")[0]

                val url_imagen_firebase=if(url_imagen!=null){
                    insertoImagen(id.toString(), url_imagen!!)
                }else{
                    "https://firebasestorage.googleapis.com/v0/b/reg-inmobiliaria-750ef.appspot.com/o/Inmobiliaria%2Fincidencia.png?alt=media&token=08f74f9b-40b6-439f-aaf5-31d92713b09c"
                }
                usuario.insertarIncidencia(id.toString(),usuario.idPiso,titulo,desc,0,url_imagen_firebase,fecha)
                usuario.insertarNotificacion(id.toString(),titulo,desc,"admin")
                db_ref.child(inmobiliaria).child(notificaionesBD).child(id.toString()).removeValue()
                usuario.runOnUiThread {
                    usuario.navController.navigate(R.id.navigation_incidencias)
                }
            }
        }
    }

    private val obtener_url= registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri?->
        when (uri){
            null-> Snackbar.make(binding.incTitulo, "Ninguna imagen selecionada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            else->{
                url_imagen=uri
                Glide.with(usuario.contexto).load(url_imagen).apply(options).into(binding.incImagen)
                Snackbar.make(binding.incTitulo, "Imagen selecionada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    suspend fun insertoImagen(id:String,foto: Uri):String{

        lateinit var urlImagenFirebase: Uri

        urlImagenFirebase= sto_ref.child(inmobiliaria).child(incidenciaBD).child(id)
            .putFile(foto).await().storage.downloadUrl.await()

        return urlImagenFirebase.toString()

    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.incTitulo, this::validoInput),
            Pair(binding.incDesc, this::validoInput)
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

    fun validoInput(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty()){
            validado=false
            v.error="Campo vacio"
        }

        return validado
    }

    fun obtenerFechaActual():String{
        val hoy = Calendar.getInstance()
        val pongoBienFecha= SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        val fecha=pongoBienFecha.format(hoy.time)

        return fecha
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}