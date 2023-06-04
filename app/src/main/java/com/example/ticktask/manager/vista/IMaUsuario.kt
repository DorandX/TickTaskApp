package com.example.ticktask.manager.vista

import com.example.ticktask.manager.vista.utills.utilManager

interface IMaUsuario:utilManager{
    suspend fun actualizarUsuario(uEmail: String, uClave:String)
    suspend fun eliminarUsuario(idUsuario:Int)
}