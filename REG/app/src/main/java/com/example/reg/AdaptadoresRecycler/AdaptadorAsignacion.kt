package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.*
import com.example.reg.Actividades.Admin
import com.example.reg.Objetos.Usuario
import com.example.reg.databinding.FilaUsuariosBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AdaptadorAsignacion(val lista:List<Usuario>, val contexto: Context): RecyclerView.Adapter<AdaptadorAsignacion.ViewHolder>(),
    Filterable {
    val SM by lazy {
        SharedManager(contexto)
    }
    var listaFiltrada = lista
    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .circleCrop()

    class ViewHolder(val bind: FilaUsuariosBinding): RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = FilaUsuariosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l = listaFiltrada[position]

        holder.bind.usuAceptar.apply {
            visibility= View.INVISIBLE
            isEnabled=false
        }

        holder.bind.usuRechazar.apply {
            visibility= View.INVISIBLE
            isEnabled=false
        }

        with(holder.bind){
            usuCorreo.text=l.correo
            usuNombre.text=l.nombre
            usuEstado.visibility=View.INVISIBLE
        }

        Glide.with(contexto).load(l.imagen).apply(options).into(holder.bind.usuImagen)
        holder.bind.usuRegistrado.visibility=View.INVISIBLE

        holder.bind.clicky.setOnClickListener {
            (contexto as Admin).apply {
                idUsu=l.id.toString()
                var id=generoId(inmobiliaria, UsuarioPisoBD)
                usuarioPisoCrear(id.toString(),l.id.toString(),contexto.idPiso)
                navController.navigate(R.id.nav_pisos)
            }

            Snackbar.make(it, "Usuario Asignado", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
                    val listaFiltrada2 = mutableListOf<Usuario>()
                    for (alu in lista) {
                        val nombreMinuscula = alu.correo!!.lowercase(Locale.ROOT)
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
                listaFiltrada = results?.values as MutableList<Usuario>
                notifyDataSetChanged()
            }
        }
    }

}