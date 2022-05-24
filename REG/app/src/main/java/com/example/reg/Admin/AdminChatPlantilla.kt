package com.example.reg.Admin

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.AdaptadoresRecycler.AdaptadorMensajes
import com.example.reg.Objetos.Mensaje
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.FragmentAdminChatPlantillaBinding
import java.text.SimpleDateFormat
import java.util.*

class AdminChatPlantilla : Fragment() {
    val admin by lazy {
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminChatPlantillaBinding? = null

    val usuario by lazy {
        admin.listaUsuarios.find { it.id==admin.idUsu }
    }

    var options = RequestOptions ()
        .fallback(R.drawable.nregistrado)
        .error(R.drawable.nregistrado)
        .circleCrop()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminChatPlantillaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed(Runnable { binding.rvChat.scrollToPosition(admin.adaptadorListaMensajes.listaFiltrada.size-1) }, 200)

        Glide.with(admin.contexto).load(usuario!!.imagen).apply(options).into(binding.chatImagen)
        binding.chatNombre.text=usuario!!.nombre

        binding.enviar.setOnClickListener {
            if(binding.mensajeEscribir.text!!.trim().isNotEmpty()){
                val mensaje=binding.mensajeEscribir.text.toString().trim()
                val hoy: Calendar = Calendar.getInstance()
                val pongoBienFecha= SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                val fecha=pongoBienFecha.format(hoy.time).split(" ")[0]

                val id_mensaje= db_ref.child(inmobiliaria).child(chatBD).child(mensajeBD).push().key!!
                val mensaje_nuevo=
                    Mensaje(id_mensaje,admin.emisor.id,usuario!!.id,mensaje,fecha)

                db_ref.child(inmobiliaria).child(chatBD).child(mensajeBD).child(id_mensaje).setValue(mensaje_nuevo)
                binding.mensajeEscribir.text!!.clear()
                binding.rvChat.scrollToPosition(admin.adaptadorListaMensajes.listaFiltrada.size-1)
            }

        }


        binding.rvChat.adapter=admin.adaptadorListaMensajes
        binding.rvChat.layoutManager= LinearLayoutManager(admin.contexto)
    }

    override fun onResume() {
        super.onResume()
        admin.adaptadorListaMensajes.receptor= usuario!!
        admin.adaptadorListaMensajes.emisor=admin.emisor
        admin.apply {
            adaptadorSalasUsuarios.filter.filter("")
            FAB_manager(5){}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}