package com.example.ticktask.utilidades

import android.content.Context
import android.widget.Toast
import com.example.ticktask.memoria.GestionDeDatos

object Info {
    private var dbManager: GestionDeDatos = GestionDeDatos().getInstance()

    //Notificación de un aviso
    fun mostrarMensaje(contexto: Context, mensaje: String){
        Toast.makeText(contexto,mensaje, Toast.LENGTH_SHORT).show()
    }

    //Notifica un aviso de error en conexion
    suspend fun errorDeConexion(contexto:Context){
        val reconectar = dbManager.tryReconnect()
        if(!reconectar){
            mostrarMensaje(contexto,"Error de conexion, prueba a reiniciar la app")
        }else{
            mostrarMensaje(contexto,"Conexion recuperada")
        }
    }
}