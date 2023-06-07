package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.interfaz.IMaDeCuenta
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.vista.interfaz.ViDeCuenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaDeCuenta : IMaDeCuenta {
    private var vista: ViDeCuenta? = null
    private lateinit var usuario: MdUsuario
    private val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeCuenta
    }

    override fun salirDeVista() {
        System.exit(0)
    }

    override suspend fun darDeBajaAUsuario(uEmail: String, uClave: String) {
        val noHayError = dbManager.eliminarUsuario(usuario.email, usuario.clave)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                vista?.darDeBajaUsuarioCorrectamente()
            } else {
                vista?.errorDeConexion()
            }
        }
    }

    override suspend fun actualizarContraseña(nClave: String, rClave: String) {
        val noHayError = dbManager.actualizarUsuario(usuario.email, usuario.clave)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                vista?.actualizarContraseñaCorrectamente()
            } else {
                vista?.errorDeConexion()
            }
        }
    }
}