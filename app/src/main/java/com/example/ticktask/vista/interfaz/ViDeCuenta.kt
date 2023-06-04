package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.MdUsuario

interface ViDeCuenta {
    fun errorDeConexion()
    fun darDeBajaUsuarioCorrectamente(usuario: MdUsuario)
    fun actualizarContraseñaCorrectamente(uEmail:String, uClave:String)
}