package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.vista.IMaUsuario
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.vista.viInterfaz.ViDeUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerDeUsuario: IMaUsuario{
    private var vista: ViDeUsuario?= null
    private var dbManager: GestionDeDatos = GestionDeDatos().getInstance()

    override fun entrarAVista(view: Activity) {
        this.vista= view as ViDeUsuario
    }
    override fun salirDeVista() {
        this.vista = null
    }
    override suspend fun actualizarUsuario(uEmail: String, uClave: String) {
        val noHayError = dbManager.actualizarDatosDeUsuario(uEmail,uClave)
        withContext(Dispatchers.Main){
            if(noHayError){
                vista?.actualizarUsuarioExitoso()
            }else{
                vista?.errorDeConexion()
            }
        }
    }

    override suspend fun eliminarUsuario(idUsuario:Int) {
        val noHayError= dbManager.eliminarUsuario(idUsuario)
        withContext(Dispatchers.Main){
            if(noHayError){
                vista?.eliminarUsuarioExitoso()
            }else{
                vista?.errorDeConexion()
            }
        }
    }


}