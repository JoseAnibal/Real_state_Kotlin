package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.Admin
import com.example.reg.R
import com.example.reg.databinding.FragmentAdminUsuariosBinding

class AdminUsuarios : Fragment() {
    val admin by lazy{
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminUsuariosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminUsuariosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.rvUsuarios.adapter=admin.adaptadorListaUsuarios
        binding.rvUsuarios.layoutManager= LinearLayoutManager(admin.contexto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}