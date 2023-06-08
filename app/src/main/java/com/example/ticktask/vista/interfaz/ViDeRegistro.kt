package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.MdUsuario

interface ViDeRegistro {
    fun errorEnConexion()
    fun existeElUsuario(usuario: MdUsuario)
    fun guardarUsuario()
    fun usuarioGuardado(usuario: MdUsuario)
}