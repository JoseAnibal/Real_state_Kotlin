package com.example.reg.Objetos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Mensaje (var id:String?=null,
               var usu_emisor:String?=null,
               var usu_receptor:String?=null,
               var texto:String?=null,
               var fecha:String?=null): Parcelable