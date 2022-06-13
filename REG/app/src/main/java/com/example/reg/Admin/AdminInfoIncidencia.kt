package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.databinding.FragmentAdminInfoIncidenciaBinding
import com.google.android.material.snackbar.Snackbar

class AdminInfoIncidencia : Fragment() {
    val admin by lazy {
        activity as Admin
    }

    val piso by lazy {
        admin.listaPisos.find { it.id==admin.incidencia.idPiso }
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminInfoIncidenciaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminInfoIncidenciaBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        val adaptador = ArrayAdapter(admin.contexto,android.R.layout.simple_spinner_item, estados).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.incSpinner.adapter = adaptador
        binding.incTitulo.text=admin.incidencia.titulo
        binding.incDesc.text=admin.incidencia.descripcion
        Glide.with(admin.contexto).load(admin.incidencia.imagenInci).into(binding.incFoto)
        binding.incSpinner.setSelection(admin.incidencia.estado!!)
        binding.incPiso.text=piso!!.calle.toString()

        binding.inciEliminar.setOnClickListener {
            db_ref.child(inmobiliaria).child(incidenciaBD).child(admin.incidencia.id!!).removeValue()
            admin.navController.navigate(R.id.nav_adminIncidencias)
            Snackbar.make(binding.incSpinner, "Incidencia eliminada", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(9,this::guardarIncidencia)
    }

    fun guardarIncidencia(v:View){
        val inc=admin.incidencia
        admin.binding.appBarAdmin.fab.hide()
        admin.insertarIncidencia(inc.id.toString(),inc.idPiso.toString(),inc.titulo.toString(),inc.descripcion.toString(),
        binding.incSpinner.selectedItemPosition,inc.imagenInci.toString(),inc.fecha.toString())
        Snackbar.make(binding.incSpinner, "Estado cambiado a: ${estados[binding.incSpinner.selectedItemPosition]}", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        admin.navController.navigate(R.id.nav_adminIncidencias)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.eliminarTodaRelacion)
        menu.removeItem(R.id.busqueda)
        menu.removeItem(R.id.modoOscuroAdmin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}