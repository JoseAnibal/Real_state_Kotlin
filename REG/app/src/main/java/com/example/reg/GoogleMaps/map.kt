package com.example.reg.GoogleMaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.reg.Actividades.Admin
import com.example.reg.R
import com.example.reg.databinding.FragmentMapBinding
import com.google.android.gms.maps.SupportMapFragment

class map: Fragment() {
    val admin by lazy{
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        createFragment()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.eliminarTodaRelacion)
        menu.removeItem(R.id.modoOscuroAdmin)
        menu.removeItem(R.id.cerrarSesion)
    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(5){}
    }

    fun createFragment(){
        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(admin.contexto)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}