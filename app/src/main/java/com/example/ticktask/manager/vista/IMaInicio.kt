package com.example.ticktask.manager.vista

import com.example.ticktask.manager.vista.utills.utilManager

interface IMaInicio: utilManager {
    suspend fun getConexion()
    suspend fun esUnUsuarioValido(uEmail: String, uClave:String)
}