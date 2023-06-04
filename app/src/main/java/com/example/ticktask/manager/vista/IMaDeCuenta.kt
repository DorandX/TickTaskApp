package com.example.ticktask.manager.vista

import com.example.ticktask.manager.vista.utills.utilManager

interface IMaDeCuenta:utilManager {
    suspend fun darDeBajaAUsuario(eUsuario: String, uClave: String)
    suspend fun actualizarContraseña(eUsuario: String, uClave: String)
}