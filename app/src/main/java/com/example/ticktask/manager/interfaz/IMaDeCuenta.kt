package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas
import com.example.ticktask.modelo.MdUsuario

interface IMaDeCuenta:IMaVistas {
    suspend fun darDeBajaAUsuario(uClave: String,uString: String)
    suspend fun actualizarContraseña(eUsuario: String, uClave: String)
}