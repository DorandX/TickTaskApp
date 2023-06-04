package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.interfaz.IMaDeCuenta
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.vista.interfaz.ViDeCuenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaDeCuenta: IMaDeCuenta {
    private var vista: ViDeCuenta?=null
    private lateinit var usuario: MdUsuario
    val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())
    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeCuenta
    }

    override fun salirDeVista() {
        this.vista= null
    }

    override suspend fun darDeBajaAUsuario(uEmail:String, uClave: String) {
        val noHayError= dbManager.eliminarUsuario(usuario.email, usuario.clave)
        withContext(Dispatchers.Main){
            if(noHayError){
                vista?.darDeBajaUsuarioCorrectamente(usuario)
            }else {
                vista?.errorDeConexion()
            }
        }
    }

    override suspend fun actualizarContraseña(eUsuario: String, uClave: String) {
        val noHayError = dbManager.actualizarUsuario(eUsuario, uClave)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                vista?.actualizarContraseñaCorrectamente(eUsuario,uClave)
            } else {
                vista?.errorDeConexion()
            }
        }
    }


}