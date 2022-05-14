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
import com.example.reg.AdaptadoresRecycler.AdaptadorFotosPiso
import com.example.reg.AdaptadoresRecycler.AdaptadorPisos
import com.example.reg.R
import com.example.reg.databinding.FragmentInfoPisoBinding

class InfoPiso : Fragment() {
    lateinit var menu: Menu
    val admin by lazy {
        activity as Admin
    }

    val piso by lazy {
        admin.listaPisos.find { it.id==admin.idPiso }
    }

    val adaptadorListaImagenesPisos by lazy {
        AdaptadorFotosPiso(piso!!.imagenes!!.toMutableList(),admin.contexto)
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentInfoPisoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentInfoPisoBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(3){}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
    }

    override fun onStart() {
        super.onStart()
        binding.pisoBaths.text=piso!!.nbaths.toString()
        binding.pisoHabs.text=piso!!.nhabs.toString()
        binding.pisoCalle.text=piso!!.calle.toString()
        binding.pisoM2.text=piso!!.m2.toString()
        binding.pisoDescripcion.text=piso!!.descripcion.toString()

        binding.rvFotosPiso.adapter=adaptadorListaImagenesPisos
        binding.rvFotosPiso.layoutManager= LinearLayoutManager(admin.contexto,LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}