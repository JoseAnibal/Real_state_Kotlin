package com.example.reg.Objetos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Piso(val id:String?=null,
           val calle:String?=null,
           val imagenes:MutableList<String>?= mutableListOf(),
           val nhabs:String?=null,
           val nbaths:String?=null,
           val m2:Double?=0.0,
           val descripcion:String?=null,
           val estado:Boolean?=false):Parcelable