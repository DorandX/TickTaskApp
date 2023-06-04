package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.interfaz.IMaInicio
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.vista.interfaz.ViDeInicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerDeInicio : IMaInicio {
    private var vista: ViDeInicio? = null
    val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeInicio
    }

    override fun salirDeVista() {
        this.vista = null
    }


    override suspend fun esUnUsuarioValido(uEmail: String, uClave: String) {
        val isValidUser = dbManager.validarUsuario(uEmail, uClave)
        if (isValidUser == null) {
            // si hay un error en la base de datos se muestra un mensaje de error
            withContext(Dispatchers.Main) {
                vista?.errorEnConexion()
            }
            return
        }
        if (isValidUser) {
            // si el usuario es valido se obtiene el id del usuario
            val idUsuario = dbManager.verificarUsuario(uEmail,uClave)
            withContext(Dispatchers.Main) {
                if (idUsuario==null) {
                    vista?.errorEnConexion()
                    return@withContext
                }
                vista?.esUnUsuarioValido(uEmail,uClave)
            }
        } else {
            // si el usuario no es valido se muestra un mensaje de error
            withContext(Dispatchers.Main) {
                vista?.esUnUsuarioNoValido()
            }
        }
    }
}
