package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.Actividades.Admin
import com.example.reg.Objetos.Incidencia
import com.example.reg.Objetos.Piso
import com.example.reg.R
import com.example.reg.databinding.FilaIncidenciaBinding
import java.util.*

class AdaptadorIncidencias(val lista:List<Incidencia>,val contexto:Context,val numero:Int):RecyclerView.Adapter<AdaptadorIncidencias.ViewHolder>(), Filterable {

    var listaFiltrada = lista
    var estadoImagen=R.drawable.nregistrado
    var filtro=0

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
            when(l.estado.toString().toInt()){
                0 -> estadoImagen=R.drawable.creada
                1 -> estadoImagen=R.drawable.enprocesopng
                2 -> estadoImagen=R.drawable.solucionada
                3 -> estadoImagen=R.drawable.rechazada
                else->{R.drawable.nregistrado}
            }
            incEstado.setImageResource(estadoImagen)
        }

        Glide.with(contexto).load(l.imagenInci).apply(options).into(holder.bind.inciImagen)

        if(numero==0){
            holder.bind.clikcyInci.setOnClickListener {
                (contexto as Admin).incidencia=l
                contexto.navController.navigate(R.id.nav_adminInfoIncidencia)
            }
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
                    val listaFiltrada2 = mutableListOf<Incidencia>()
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

                listaFiltrada= if(filtro!=0){
                    when(filtro){
                        1->listaFiltrada.filter { it.estado==1 }
                        2->listaFiltrada.filter { it.estado==2 }
                        3->listaFiltrada.filter { it.estado==3 }
                        4->listaFiltrada
                        else -> listaFiltrada
                    }
                }else{
                    listaFiltrada.filter { it.estado==0 }
                }

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