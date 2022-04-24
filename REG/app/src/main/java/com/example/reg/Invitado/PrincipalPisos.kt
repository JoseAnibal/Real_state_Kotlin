package com.example.reg.Invitado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.LoggedUser
import com.example.reg.AdaptadoresRecycler.AdaptadorPisos
import com.example.reg.Objetos.Piso
import com.example.reg.databinding.FragmentPrincipalPisosBinding
import com.example.reg.db_ref
import com.example.reg.inmobiliaria
import com.example.reg.pisosBD
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PrincipalPisos : Fragment() {

    val logged by lazy {
        activity as LoggedUser
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentPrincipalPisosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentPrincipalPisosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.rvPisos.adapter=logged.adaptadorListaPisos
        binding.rvPisos.layoutManager= LinearLayoutManager(logged.baseContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}