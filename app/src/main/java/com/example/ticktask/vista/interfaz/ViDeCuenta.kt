package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.MdUsuario

interface ViDeCuenta {
    fun errorDeConexion()
    fun finalizarVista()
    fun darDeBajaUsuarioCorrectamente()
    fun actualizarContraseñaCorrectamente()
}