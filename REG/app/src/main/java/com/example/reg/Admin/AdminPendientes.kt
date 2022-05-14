package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.Admin
import com.example.reg.R
import com.example.reg.databinding.FragmentAdminPendientesBinding

class AdminPendientes : Fragment() {

    val admin by lazy{
        activity as Admin
    }
    lateinit var menu: Menu
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminPendientesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminPendientesBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        (menu.findItem(R.id.busqueda).actionView as SearchView).setQuery("", false)
        (menu.findItem(R.id.busqueda).actionView as SearchView).clearFocus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.rvPendientes.adapter=admin.adaptadorListaUsuNoRegistrados
        binding.rvPendientes.layoutManager= LinearLayoutManager(admin.contexto)
    }

    override fun onResume() {
        super.onResume()
        admin.apply {
            adaptadorListaUsuarios.filter.filter("")
            FAB_manager(5){}
        }
    }

    fun refreshFilter(){
        val query=(menu.findItem(R.id.busqueda).actionView as SearchView).query
        admin.adaptadorListaUsuNoRegistrados.filter.filter(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}