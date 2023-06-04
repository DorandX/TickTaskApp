package com.example.ticktask.manager.vista

import com.example.ticktask.manager.vista.utills.utilManager
import com.example.ticktask.modelo.MdUsuario

interface IMaRegistro:utilManager {
    suspend fun verificarSiExisteUsuario(uEmail: String, uClave:String)
    suspend fun agregarUsuario(usuario: MdUsuario)
}