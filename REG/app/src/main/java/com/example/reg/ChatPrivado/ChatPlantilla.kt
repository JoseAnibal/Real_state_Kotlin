package com.example.reg.ChatPrivado

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.Actividades.MainActivity
import com.example.reg.Objetos.Mensaje
import com.example.reg.databinding.FragmentChatPlantillaBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatPlantilla : Fragment() {
    val usuario by lazy {
        activity as MainActivity
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentChatPlantillaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //Glide.with(contexto).load(objeto.img).apply((contexto as MainActivity).options)
    //.into(holder.bing.imgPlaneta)
    //lateinit var options:RequestOptions
    //fitCenter()
    //centerCrop()
    //circleCrop()
    //centerInside()
    val options = RequestOptions ()
        .fallback(R.drawable.nregistrado)
        .error(R.drawable.nregistrado)
        .circleCrop()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentChatPlantillaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        usuario.FAB_manager(0){}
        binding.rvChat.scrollToPosition(usuario.listaMensajes.size-1)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed(Runnable { binding.rvChat.scrollToPosition(usuario.listaMensajes.size-1) }, 200)

        Glide.with(usuario.contexto).load(usuario.receptor.imagen).apply(options).into(binding.chatImagen)
        binding.chatNombre.text=usuario.receptor.nombre

        binding.enviar.setOnClickListener {
            if(binding.mensajeEscribir.text!!.trim().isNotEmpty()){
                val mensaje=binding.mensajeEscribir.text.toString().trim()
                val hoy: Calendar = Calendar.getInstance()
                val pongoBienFecha= SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                val fecha=pongoBienFecha.format(hoy.time)

                val id_mensaje= db_ref.child(inmobiliaria).child(chatBD).child(mensajeBD).push().key!!
                val mensaje_nuevo=Mensaje(id_mensaje,usuario.emisor.id,usuario.receptor.id,mensaje,fecha)

                db_ref.child(inmobiliaria).child(chatBD).child(mensajeBD).child(id_mensaje).setValue(mensaje_nuevo)
                binding.mensajeEscribir.text!!.clear()
                binding.rvChat.scrollToPosition(usuario.listaMensajes.size-1)
            }

        }


        binding.rvChat.adapter=usuario.adaptadorListaMensajes
        binding.rvChat.layoutManager= LinearLayoutManager(usuario.contexto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}