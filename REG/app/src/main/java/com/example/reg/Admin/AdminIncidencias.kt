package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.Actividades.Admin
import com.example.reg.R
import com.example.reg.databinding.FragmentAdminIncidenciasBinding
import com.example.reg.estados

class AdminIncidencias : Fragment() {
    val admin by lazy {
        activity as Admin
    }
    lateinit var menu: Menu

    val estados= mutableListOf("Creada","En proceso","Solucionada","Rechazada","Todas")
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminIncidenciasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminIncidenciasBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        menu.removeItem(R.id.eliminarTodaRelacion)
        menu.removeItem(R.id.modoOscuroAdmin)
    }

    override fun onStart() {
        super.onStart()
        val adaptador = ArrayAdapter(admin.contexto,android.R.layout.simple_spinner_item, estados).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spiFliltrado.adapter = adaptador
        binding.spiFliltrado.setSelection(4)
        binding.spiFliltrado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                admin.adaptadorListaIncidencias.filtro=position
                refreshFilter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.rvIncidencias.adapter=admin.adaptadorListaIncidencias
        binding.rvIncidencias.layoutManager= LinearLayoutManager(admin.contexto)
    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(5){}
        admin.adaptadorListaIncidencias.filter.filter("")
    }

    fun refreshFilter(){
        val query=(menu.findItem(R.id.busqueda).actionView as SearchView).query
        admin.adaptadorListaIncidencias.filter.filter(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}