package com.example.reg.Usuario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.MainActivity
import com.example.reg.R
import com.example.reg.databinding.FragmentFacturasBinding
import com.example.reg.databinding.FragmentIncidenciasBinding

class Facturas : Fragment() {
    val usu by lazy {
        activity as MainActivity
    }
    //FragmentNombrefragmento
    private var _binding: FragmentFacturasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //FragmentNombrefragmento
        _binding = FragmentFacturasBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.rvFacturasUsu.adapter=usu.adaptadorListaFacturas
        binding.rvFacturasUsu.layoutManager= LinearLayoutManager(usu.contexto)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.modoOscuro)

    }

    override fun onResume() {
        super.onResume()
        usu.FAB_manager(0){}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}