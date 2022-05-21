package com.example.reg.Objetos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Usuario(val id:String?=null,
              val correo:String?=null,
              val nombre:String?=null,
              val password:String?=null,
              val tipo:Int?=null,
              val imagen:String?=null,
              val resgistrado:Boolean?=false): Parcelable