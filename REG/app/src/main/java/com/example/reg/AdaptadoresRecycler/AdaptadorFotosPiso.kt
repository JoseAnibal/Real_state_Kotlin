package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.Actividades.Admin
import com.example.reg.R
import com.example.reg.databinding.FilaFotospisoBinding

class AdaptadorFotosPiso(val lista:MutableList<String>, val contexto: Context): RecyclerView.Adapter<AdaptadorFotosPiso.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
    //EmpiezaEnMayuscula
        val bind = FilaFotospisoBinding.bind(v)
    }
                                                                       //Recycler.ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorFotosPiso.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fila_fotospiso,parent,false)
        return ViewHolder(v)
    }
                                        //Recycler.ViewHolder
    override fun onBindViewHolder(holder: AdaptadorFotosPiso.ViewHolder, position: Int) {
        val l = lista[position]
        Glide.with(contexto).load(l).into(holder.bind.pisoImagen)

    }

    override fun getItemCount(): Int {
        return lista.size
    }


}