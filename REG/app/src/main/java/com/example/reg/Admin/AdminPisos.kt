package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.Admin
import com.example.reg.Objetos.Piso
import com.example.reg.R
import com.example.reg.databinding.FragmentAdminPisosBinding
import com.example.reg.db_ref
import com.example.reg.inmobiliaria
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminPisos : Fragment() {

    val admin by lazy {
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminPisosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminPisosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        binding.rvAdminPisos.adapter=admin.adaptadorListaPisos
        binding.rvAdminPisos.layoutManager= LinearLayoutManager(admin.baseContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}