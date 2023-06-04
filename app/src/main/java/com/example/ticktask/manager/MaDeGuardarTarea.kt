package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.interfaz.IMaDeGuardarTarea
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.vista.interfaz.ViDeGuardarTarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaDeGuardarTarea : IMaDeGuardarTarea {
    private var vista: ViDeGuardarTarea? = null
    val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeGuardarTarea
    }

    override fun salirDeVista() {
        this.vista = null
    }

    override suspend fun validarTarea(tarea: MdTarea) {
        val existeLaTarea = dbManager.verificarSiExisteTarea(tarea.idDeTarea.toString())
        withContext(Dispatchers.Main) {
            when (existeLaTarea) {
                null -> {
                    //si existe la tarea, manda un mensaje de error
                    vista?.errorDeConexion()
                }
                true -> {
                    //Si existe, manda un aviso
                    vista?.existeLaTarea()
                }
                else -> {
                    //Si no existe, la crea.
                    vista?.guardarTarea()
                }
            }
        }
    }



    override suspend fun guardarTareaEnMemoria(tarea: MdTarea) {
        val noHayError = dbManager.crearTarea(tarea)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                //si no hay error al registral el usuario
                vista?.guardarTarea()
            } else {
                vista?.errorDeConexion()
            }
        }
    }


}
