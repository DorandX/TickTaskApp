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
        System.exit(0)
    }


    override suspend fun esUnUsuarioValido(uEmail: String, uClave: String): Boolean {
        val isValidUser = dbManager.validarUsuario(uEmail, uClave)
        return when {
            isValidUser == null -> {
                withContext(Dispatchers.Main) {
                    vista?.errorEnConexion()
                }
                false
            }
            isValidUser -> {
                val idUsuario = dbManager.verificarUsuario(uEmail, uClave)
                withContext(Dispatchers.Main) {
                    if (idUsuario == null) {
                        vista?.errorEnConexion()
                        return@withContext false
                    }
                    vista?.esUnUsuarioValido(uEmail, uClave)
                }
                true
            }
            else -> {
                withContext(Dispatchers.Main) {
                    vista?.esUnUsuarioNoValido()
                }
                false
            }
        }
    }
}
