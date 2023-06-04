package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas
import com.example.ticktask.modelo.MdUsuario

interface IMaRegistro:IMaVistas {
    suspend fun verificarSiExisteUsuario(uEmail: String, uClave:String)
    suspend fun agregarUsuario(usuario: MdUsuario)
}