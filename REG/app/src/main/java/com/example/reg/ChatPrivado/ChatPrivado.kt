package com.example.reg.ChatPrivado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.AdaptadoresRecycler.AdaptadorMensajes
import com.example.reg.AdaptadoresRecycler.AdaptadorUsuariosNoRegistrados
import com.example.reg.Objetos.Mensaje
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.ActivityChatPrivadoBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChatPrivado : AppCompatActivity() {
    val SM by lazy {
        SharedManager(this)
    }

    val listaMensajes by lazy {
        añadoListaMensajes()
    }

    val adaptadorListaMensajes by lazy {
        AdaptadorMensajes(listaMensajes,this,emisor,receptor)
    }

    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .circleCrop()
    lateinit var binding:ActivityChatPrivadoBinding
    lateinit var receptor:Usuario
    lateinit var emisor:Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding= ActivityChatPrivadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        receptor= Usuario()
        emisor= Usuario()
        GlobalScope.launch(Dispatchers.IO){
            receptor= sacoUsuarioDeLaBase("jose@gmail.com")
            runOnUiThread {
                Glide.with(this@ChatPrivado).load(receptor.imagen).apply(options).into(binding.chatImagen)
                binding.chatNombre.text=receptor.nombre
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val idUsu= SM.idUsuario

        val bundle = intent.extras
        emisor = bundle?.getParcelable<Usuario>("UsuarioActual")?: Usuario()

        binding.rvChat.adapter=adaptadorListaMensajes
        binding.rvChat.layoutManager= LinearLayoutManager(this)
        binding.rvChat.scrollToPosition(listaMensajes.lastIndex)

        binding.enviar.setOnClickListener {
            if(binding.mensajeEscribir.text!!.trim().isNotEmpty()){
                val mensaje=binding.mensajeEscribir.text.toString().trim()
                val hoy: Calendar = Calendar.getInstance()
                val pongoBienFecha= SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                val fecha=pongoBienFecha.format(hoy.time)

                val id_mensaje=db_ref.child(inmobiliaria).child("Chat").child("Mensajes").push().key!!
                val mensaje_nuevo=Mensaje(id_mensaje,emisor.id,receptor.id,mensaje,fecha)

                db_ref.child(inmobiliaria).child("Chat").child("Mensajes").child(id_mensaje).setValue(mensaje_nuevo)
                binding.mensajeEscribir.text!!.clear()
                binding.rvChat.scrollToPosition(listaMensajes.lastIndex)
            }

        }

    }

    fun añadoListaMensajes():MutableList<Mensaje>{
        val lista= mutableListOf<Mensaje>()

        db_ref.child(inmobiliaria).child("Chat")
            .child("Mensajes")
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

    override fun onResume() {
        super.onResume()

    }
}