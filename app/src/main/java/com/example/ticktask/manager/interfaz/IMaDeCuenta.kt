package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas

interface IMaDeCuenta:IMaVistas {
    suspend fun darDeBajaAUsuario(uEmail: String, uClave: String)
    suspend fun actualizarContraseña(nClave: String, rClave: String)
}