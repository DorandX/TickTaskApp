package com.example.ticktask.modelo

import android.os.Parcel
import android.os.Parcelable

data class MdUsuario(
    val idUsuario: Int,
    val nombre : String,
    val apellido : String,
    val email: String,
    val telefono: Int,
    val clave: String
)

