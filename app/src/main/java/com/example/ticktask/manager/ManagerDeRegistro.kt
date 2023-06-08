package com.example.ticktask.manager

import android.app.Activity

import com.example.ticktask.manager.interfaz.IMaRegistro
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.vista.interfaz.ViDeRegistro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManagerDeRegistro : IMaRegistro {

    private var vista: ViDeRegistro? = null
    private lateinit var usuario: MdUsuario
    val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    //Metodo para asociar manager con la interfaz
    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeRegistro
    }

    //Método para desasociar manager con la interfaz
    override fun salirDeVista() {
        System.exit(0)
    }

    //Verificamos si existe o no el usuario.
    override suspend fun verificarSiExisteUsuario(uEmail: String, uClave: String) {
        val existeUsuario = dbManager.verificarUsuario(uEmail, uClave)
        withContext(Dispatchers.Main) {
            when (existeUsuario) {
                null -> {
                    // si hay error de conexión con la base de datos
                    vista?.errorEnConexion()
                }
                true -> {
                    // si el usuario ya existe
                    vista?.existeElUsuario(usuario)
                }
                else -> {
                    // si el usuario no existe
                    vista?.guardarUsuario()
                }
            }
        }
    }

    //Metodo para agregar usuario a la memoria
    override suspend fun agregarUsuario(usuario: MdUsuario) {
        val noHayError = dbManager.guardarUsuario(usuario)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                //si no hay error al registral el usuario
                vista?.usuarioGuardado(usuario)
            } else {
                vista?.errorEnConexion()
            }
        }
    }


}