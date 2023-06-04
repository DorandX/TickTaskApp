package com.example.ticktask.vista.interfaz

interface ViDeInicio {
    fun errorEnConexion()
    fun esUnUsuarioValido(idUsuario: String, uClave: String)
    fun esUnUsuarioNoValido()
}