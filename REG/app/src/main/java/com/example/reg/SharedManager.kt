package com.example.reg

import android.content.Context

class SharedManager(val context: Context) {
    val ID = "com.example.reg"
    val shared_name = "${ID}_sp"
    val SP = context.getSharedPreferences(shared_name, 0)

    var idUsuario: String
        get() = SP.getString(
            context.getString(R.string.idUsuario),
            context.getString(R.string.idUsuarioDef)
        ) ?: context.getString(R.string.idUsuarioDef)

        set(value) = SP.edit().putString(
            context.getString(R.string.idUsuario),
            value)
            .apply()

}