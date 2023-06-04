package com.example.ticktask.utilidades

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Info {
    //Notificación de un
    private var dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    fun mostrarMensaje(contexto: Context, mensaje: String) {
        (contexto as Activity).runOnUiThread {
            Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    //Notifica un aviso de error en conexion
    suspend fun errorDeConexion(contexto: Context) {
        withContext(Dispatchers.Main) {
            mostrarMensaje(contexto, "Error de conexión, prueba a reiniciar la app")
        }
    }
}