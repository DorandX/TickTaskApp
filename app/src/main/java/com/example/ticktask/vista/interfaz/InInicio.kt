package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.Usuario

interface InInicio {

    fun salir()
    fun errorDeConexion()
    fun conexionExitosa()
    fun guardarUsuario()
    fun usuarioGuardado(usuario: Usuario)

}