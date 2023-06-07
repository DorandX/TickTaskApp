package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas

interface IMaInicio: IMaVistas {

    suspend fun esUnUsuarioValido(uEmail: String, uClave:String): Boolean
}