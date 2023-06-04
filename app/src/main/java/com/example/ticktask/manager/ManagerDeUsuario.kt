package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.interfaz.IMaUsuario
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.vista.interfaz.ViDeUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerDeUsuario: IMaUsuario{
    private var vista: ViDeUsuario?= null
    val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())
    override fun entrarAVista(view: Activity) {
        this.vista= view as ViDeUsuario
    }
    override fun salirDeVista() {
        this.vista = null
    }
    override suspend fun actualizarUsuario(uEmail: String, uClave: String) {
        val noHayError = dbManager.actualizarUsuario(uEmail,uClave)
        withContext(Dispatchers.Main){
            if(noHayError){
                vista?.actualizarUsuarioExitoso()
            }else{
                vista?.errorDeConexion()
            }
        }
    }

    override suspend fun eliminarUsuario(uEmail: String, uClave: String) {
        val noHayError= dbManager.eliminarUsuario(uEmail,uClave)
        withContext(Dispatchers.Main){
            if(noHayError){
                vista?.eliminarUsuarioExitoso()
            }else{
                vista?.errorDeConexion()
            }
        }
    }


}