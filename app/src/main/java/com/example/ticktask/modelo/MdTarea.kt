package com.example.ticktask.modelo

import android.os.Parcel
import android.os.Parcelable
import java.sql.Date

data class MdTarea(
    val idDeTarea: Int,
    val idDeUsuario: MdUsuario,
    val titulo: String,
    val descripcion: String?,
    val prioridad: String,
    val estado: String,
    val entrega: Date?= null
)
