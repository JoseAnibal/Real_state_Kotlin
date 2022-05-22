package com.example.reg.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.databinding.FragmentAdminAddFacturaBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AdminAddFactura : Fragment() {
    val admin by lazy{
        activity as Admin
    }
                          //FragmentNombrefragmento
    private var _binding: FragmentAdminAddFacturaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var addfactura=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                   //FragmentNombrefragmento
        _binding = FragmentAdminAddFacturaBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
    }

    fun insertarFactura(v:View){
        if(isValid()){
            GlobalScope.launch(Dispatchers.IO) {
                val id= generoId(inmobiliaria, facturaBD)
                val agua=binding.facAgua.text.toString()
                val luz=binding.facLuz.text.toString()
                val internet=binding.facInternet.text.toString()
                val gastosExtra=binding.facGastosExtra.text!!.trim().toString()
                val total=if(binding.facTotal.text!!.isEmpty()){
                    (agua.toDouble()+luz.toDouble()+internet.toDouble()).toString()
                }else{
                    binding.facTotal.text.toString().toDouble().toString()
                }
                val fecha=obtenerFechaActual().split(" ")[0]
                admin.runOnUiThread { admin.binding.appBarAdmin.fab.hide() }
                admin.runOnUiThread { Snackbar.make(binding.facAgua, "Factura creada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show() }
                admin.insertoFactura(id!!,admin.idPiso,luz,agua,internet,gastosExtra,total.toString(),fecha)
                admin.runOnUiThread { admin.navController.navigate(R.id.nav_adminUsuFacturas)}
            }
        }
    }

    fun isValid():Boolean{
        var validado = true
        val checkers = listOf(
            Pair(binding.facAgua, this::validoInput),
            Pair(binding.facLuz, this::validoInput),
            Pair(binding.facInternet, this::validoInput)
        )
        for(c in checkers){
            val x = c.first
            val f = c.second
            val y = f(x)
            validado = y
            if(!validado) break
        }
        return validado
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.removeItem(R.id.busqueda)
    }

    override fun onResume() {
        super.onResume()
        admin.FAB_manager(8,this::insertarFactura)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun validoInput(v: EditText):Boolean{
        var validado=true

        if(v.text.isEmpty()){
            validado=false
            v.error="Campo vacio"
        }

        return validado
    }

    fun obtenerFechaActual():String{
        val hoy = Calendar.getInstance()
        val pongoBienFecha= SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        val fecha=pongoBienFecha.format(hoy.time)

        return fecha
    }
}