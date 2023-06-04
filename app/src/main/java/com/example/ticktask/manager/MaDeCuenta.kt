package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.vista.IMaDeCuenta
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.vista.viInterfaz.ViDeCuenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaDeCuenta: IMaDeCuenta {
    private var vista: ViDeCuenta?=null
    private var dbManager: GestionDeDatos= GestionDeDatos().getInstance()

    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeCuenta
    }

    override fun salirDeVista() {
        this.vista= null
    }

    override suspend fun darDeBajaAUsuario(eUsuario: String, uClave: String) {
        val noHayError= dbManager.eliminarUsuario(eUsuario,uClave)
        withContext(Dispatchers.Main){
            if(noHayError){
                vista?.darDeBajaUsuarioCorrectamente()
            }else {
                vista?.errorDeConexion()
            }
        }
    }

    override suspend fun actualizarContraseña(eUsuario: String, uClave: String) {
        val noHayError = dbManager.actualizarDatosDeUsuario(eUsuario, uClave)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                vista?.darDeBajaUsuarioCorrectamente()
            } else {
                vista?.errorDeConexion()
            }
        }
    }


}