package com.example.reg.AdaptadoresRecycler

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.reg.ChatPrivado.ChatPrivado
import com.example.reg.Objetos.Mensaje
import com.example.reg.Objetos.Usuario
import com.example.reg.R
import com.example.reg.databinding.FilaMensajeBinding

class AdaptadorMensajes(var lista:MutableList<Mensaje>, val contexto: Context, val emisor:Usuario,val receptor:Usuario): RecyclerView.Adapter<AdaptadorMensajes.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
    //EmpiezaEnMayuscula
        val bind = FilaMensajeBinding.bind(v)
    }
    val options = RequestOptions ()
        .fallback(R.drawable.common_full_open_on_phone)
        .error(R.drawable.common_full_open_on_phone)
        .circleCrop()
                                                                       //Recycler.ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorMensajes.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fila_mensaje,parent,false)
        return ViewHolder(v)
    }
                                        //Recycler.ViewHolder
    override fun onBindViewHolder(holder: AdaptadorMensajes.ViewHolder, position: Int) {
        val l = lista[position]
        holder.bind.emisorImagen.visibility=View.VISIBLE
        holder.bind.receptorImagen.visibility=View.VISIBLE

        if(l.usu_emisor==emisor.id){
            with(holder.bind){
                receptorFecha.text=""
                receptorMensaje.text=""
                receptorImagen.visibility=View.INVISIBLE

                emisorFecha.text=l.fecha
                emisorMensaje.text=l.texto
            }

            Glide.with(contexto).load(emisor.imagen).apply(options).into(holder.bind.emisorImagen)
        }else{
            with(holder.bind){
                receptorFecha.text=l.fecha
                receptorMensaje.text=l.texto

                emisorFecha.text=""
                emisorMensaje.text=""
                emisorImagen.visibility=View.INVISIBLE
            }
            Glide.with(contexto).load(receptor.imagen).apply(options).into(holder.bind.receptorImagen)
        }


    }

    override fun getItemCount(): Int {
        return lista.size
    }


}