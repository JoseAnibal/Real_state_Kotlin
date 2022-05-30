package com.example.reg.Usuario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.reg.Actividades.MainActivity
import com.example.reg.R
import com.example.reg.databinding.FragmentInfoFacturaUsuarioBinding
import com.example.reg.db_ref
import com.example.reg.facturaBD
import com.example.reg.inmobiliaria
import com.google.android.material.snackbar.Snackbar

class InfoFacturaUsuario : Fragment() {
    val usu by lazy {
        activity as MainActivity
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentInfoFacturaUsuarioBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentInfoFacturaUsuarioBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        binding.infoFecha.text=usu.facturilla.fecha
        binding.infoAgua.text=resources.getString(R.string.precio,usu.facturilla.agua)
        binding.infoLuz.text=resources.getString(R.string.precio,usu.facturilla.luz)
        binding.infoInternet.text=resources.getString(R.string.precio,usu.facturilla.internet)
        binding.infoTotal.text=resources.getString(R.string.precio,usu.facturilla.total)
        binding.infoGastosExtra.text=usu.facturilla.gastosExtra

    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.modoOscuro)
        menu.removeItem(R.id.estadisticaFactura)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}