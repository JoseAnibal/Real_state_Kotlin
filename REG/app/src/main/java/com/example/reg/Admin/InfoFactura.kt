package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.databinding.FragmentInfoFacturaBinding
import com.google.android.material.snackbar.Snackbar

class InfoFactura : Fragment() {
    val admin by lazy {
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentInfoFacturaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentInfoFacturaBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        binding.infoFecha.text=admin.facturilla.fecha
        binding.infoAgua.text=resources.getString(R.string.precio,admin.facturilla.agua)
        binding.infoLuz.text=resources.getString(R.string.precio,admin.facturilla.luz)
        binding.infoInternet.text=resources.getString(R.string.precio,admin.facturilla.internet)
        binding.infoTotal.text=resources.getString(R.string.precio,admin.facturilla.total)
        binding.infoGastosExtra.text=admin.facturilla.gastosExtra

        binding.infoEliminar.setOnClickListener {
            admin.facturilla.id?.let { it1 -> db_ref.child(inmobiliaria).child(facturaBD).child(it1).removeValue() }
            admin.navController.navigate(R.id.nav_adminUsuFacturas)
            Snackbar.make(it, "Factura eliminada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.eliminarTodaRelacion)
    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(5){}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}