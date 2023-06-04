package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas

interface IMaUsuario:IMaVistas{
    suspend fun actualizarUsuario(uEmail: String, uClave:String)
    suspend fun eliminarUsuario(uEmail: String,uClave: String)
}