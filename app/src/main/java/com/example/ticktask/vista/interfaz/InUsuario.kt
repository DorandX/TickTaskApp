package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.Usuario

interface InUsuario {
    fun conexionExitosa()
    fun errorDeConexion()
    fun guardarUsuario()
    fun usuarioGuardado(usuario: Usuario)
}