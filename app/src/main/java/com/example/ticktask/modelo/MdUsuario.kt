package com.example.ticktask.modelo

data class MdUsuario(
    val idUsuario: Int=0,
    val nombre : String,
    val apellido : String,
    val email: String,
    val telefono: Int,
    val clave: String
)
