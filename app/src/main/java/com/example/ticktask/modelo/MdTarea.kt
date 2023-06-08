package com.example.ticktask.modelo

data class MdTarea(
    val idDeTarea: Int,
    val idDeUsuario: Int,
    val titulo: String,
    val descripcion: String?,
    val prioridad: String,
    val estado: String,
    val entrega: java.util.Date?
)
