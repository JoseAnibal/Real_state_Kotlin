package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.Admin
import com.example.reg.AdaptadoresRecycler.AdaptadorAsignacion
import com.example.reg.AdaptadoresRecycler.AdaptadorUsuariosLista
import com.example.reg.Objetos.Usuario
import com.example.reg.Objetos.UsuarioPiso
import com.example.reg.R
import com.example.reg.UsuarioPisoBD
import com.example.reg.databinding.FragmentAdminUsuariosEnPisoBinding
import com.example.reg.db_ref
import com.example.reg.inmobiliaria
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminUsuariosEnPiso : Fragment() {
    val admin by lazy {
        activity as Admin
    }

    val listaUsuariosEnPiso by lazy {
        añadoListaUsuariosEnPiso(admin.idPiso)
    }

    val adaptadorListaUsuariosEnPiso by lazy {
        AdaptadorUsuariosLista(listaUsuariosEnPiso,admin.contexto,1)
    }

                          //FragmentNombrefragmento
    private var _binding: FragmentAdminUsuariosEnPisoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminUsuariosEnPisoBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(5){}
    }

    override fun onStart() {
        super.onStart()
        binding.rvUsuariosEnPiso.adapter=adaptadorListaUsuariosEnPiso
        binding.rvUsuariosEnPiso.layoutManager= LinearLayoutManager(admin.contexto)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
    }

    fun añadoListaUsuariosEnPiso(idPiso:String):MutableList<Usuario>{
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
                        if (ussu != null && ussu.idPiso==idPiso) {
                            ussu.idUsuario?.let { lista.add(it) }
                        }
                    }

                    admin.adaptadorListaUsuarios.lista.forEachIndexed { i, u ->
                        if(lista.contains(u.id)){
                            listadevolver.add(u)
                        }
                    }
                    //ACTUALIZAR ADAPTADOR
                    adaptadorListaUsuariosEnPiso.notifyItemChanged(listaUsuariosEnPiso.size)
                    adaptadorListaUsuariosEnPiso.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return listadevolver
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}