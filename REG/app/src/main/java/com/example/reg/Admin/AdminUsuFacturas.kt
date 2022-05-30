package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.AdaptadoresRecycler.AdaptadorFacturas
import com.example.reg.Objetos.Factura
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.FragmentAdminUsuFacturasBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminUsuFacturas : Fragment() {

    val admin by lazy{
        activity as Admin
    }

    val listaFacturas by lazy {
        añadoFacturas()
    }

    val adaptadorFacturas by lazy {
        AdaptadorFacturas(listaFacturas,admin.contexto)
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminUsuFacturasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminUsuFacturasBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        binding.rvFacturas.adapter=adaptadorFacturas
        binding.rvFacturas.layoutManager= LinearLayoutManager(admin.contexto)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.eliminarTodaRelacion)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.modoOscuroAdmin)
    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(7){}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    fun añadoFacturas():MutableList<Factura>{
        val lista= mutableListOf<Factura>()

        db_ref.child(inmobiliaria)
            .child(facturaBD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    snapshot.children.forEach{ hijo: DataSnapshot?->

                        val ffac=hijo?.getValue(Factura::class.java)
                        if (ffac != null && ffac.idPiso==admin.idPiso) {
                            lista.add(ffac)
                        }
                    }
                    adaptadorFacturas.notifyItemChanged(listaFacturas.size)
                    adaptadorFacturas.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            })
        return lista
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}