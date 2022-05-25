package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.reg.Actividades.MainActivity
import com.example.reg.Objetos.Factura
import com.example.reg.Objetos.Incidencia
import com.example.reg.R
import com.example.reg.databinding.FilaFacturaBinding
import java.util.*

class AdaptadorFacturasUsuario(val lista:List<Factura>,val contexto:Context):RecyclerView.Adapter<AdaptadorFacturasUsuario.ViewHolder>(), Filterable {

    var listaFiltrada = lista

    class ViewHolder(val bind: FilaFacturaBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaFacturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]
        var mes="Enero"
        when(l.fecha!!.split("-")[1]){
            "01"->mes="Enero"
            "02"->mes="Febrero"
            "03"->mes="Marzo"
            "04"->mes="Abril"
            "05"->mes="Mayo"
            "06"->mes="Junio"
            "07"->mes="Julio"
            "08"->mes="Agosto"
            "09"->mes="Septiembre"
            "10"->mes="Octubre"
            "11"->mes="Noviembre"
            "12"->mes="Diciembre"

        }
        with(holder.bind){
            factFecha.text=mes
            factTotal.text=l.total
        }


        holder.bind.clickyf.setOnClickListener {
            (contexto as MainActivity).facturilla=l
            contexto.navController.navigate(R.id.navigation_infoFacturaUsuario)
        }

    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(busqueda: CharSequence?): FilterResults {
                val texto = busqueda.toString()
                //Filtro 1, el clásico de por nombre, no hace falta pensar
                if (texto.isEmpty()) {
                    listaFiltrada = lista
                } else {
                    val listaFiltrada2 = mutableListOf<Factura>()
                    for (alu in lista) {
                        val nombreMinuscula = alu.fecha!!.lowercase(Locale.ROOT)
                        val textoMinuscula = texto.lowercase(Locale.ROOT)
                        if (nombreMinuscula.contains(textoMinuscula)) {
                            listaFiltrada2.add(alu)
                        }
                    }
                    listaFiltrada = listaFiltrada2
                }
                //FILTROS AQUI

                val filterResults = FilterResults()
                filterResults.values = listaFiltrada
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = results?.values as MutableList<Factura>
                notifyDataSetChanged()
            }
        }
    }

}