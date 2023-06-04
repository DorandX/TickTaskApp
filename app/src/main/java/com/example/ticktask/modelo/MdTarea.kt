package com.example.ticktask.modelo

import java.sql.Date

data class MdTarea(
    val idDeTarea: Int = 0,
    val idDeUsuario: MdUsuario,
    val titulo: String,
    val descripcion: String?= null,
    val prioridad: String,
    val estado: String,
    val entrega: Date?= null
)
