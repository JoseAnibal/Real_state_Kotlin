package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.Objetos.Incidencia
import com.example.reg.R
import com.example.reg.databinding.FilaIncidenciaBinding

class AdaptadorIncidencias(val lista:List<Incidencia>,val contexto:Context):RecyclerView.Adapter<AdaptadorIncidencias.ViewHolder>(), Filterable {

    var listaFiltrada = lista

    class ViewHolder(val bind: FilaIncidenciaBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaIncidenciaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    var options = RequestOptions ()
        .fallback(R.drawable.nregistrado)
        .error(R.drawable.nregistrado)
        .circleCrop()

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
            inciTitulo.text=l.titulo
            inciFecha.text=l.fecha
        }

        Glide.with(contexto).load(l.imagenInci).apply(options).into(holder.bind.inciImagen)

    }

    override fun getItemCount(): Int {
        return listaFiltrada.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(busqueda: CharSequence?): FilterResults {
                //FILTROS AQUI

                val filterResults = FilterResults()
                filterResults.values = listaFiltrada
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaFiltrada = results?.values as MutableList<Incidencia>
                notifyDataSetChanged()
            }
        }
    }

}