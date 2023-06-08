package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.interfaz.IMaDeCrearTarea
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.vista.interfaz.ViDeGuardarTarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class MaDeCrearTarea : IMaDeCrearTarea {
    private lateinit var vista: ViDeGuardarTarea
    private val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeGuardarTarea
    }

    override fun salirDeVista() {
       vista.finalizarActividad()
    }

    override suspend fun validarTarea(tarea: MdTarea) {
        val existeLaTarea = dbManager.verificarSiExisteTarea(tarea.idDeTarea.toString())
        withContext(Dispatchers.Main) {
            when (existeLaTarea) {
                null -> {
                    //si existe la tarea, manda un mensaje de error
                    vista.errorDeConexion()
                }
                true -> {
                    //Si existe, manda un aviso
                    vista.existeLaTarea(tarea)
                }
                else -> {
                    //Si no existe, la crea.
                    vista.guardarTarea()
                }
            }
        }
    }

    override suspend fun guardarTareaEnMemoria(tarea: MdTarea) {
        try {
            val tareaCreadaExitosamente = dbManager.crearTarea(tarea)
            withContext(Dispatchers.Main) {
                if (tareaCreadaExitosamente) {
                    //si no hay error al registral el usuario
                    vista.guardarTarea()
                } else {
                    vista.errorDeConexion()
                }
            }
        } catch (e: IOException) {
            // Maneja específicamente los errores de red o de entrada/salida
            withContext(Dispatchers.Main) {
                vista.errorDeConexion()
            }
        } catch (e: Exception) {
            // Maneja todas las otras excepciones
            withContext(Dispatchers.Main) {
                vista.errorDeConexion()
            }
        }
    }
}



