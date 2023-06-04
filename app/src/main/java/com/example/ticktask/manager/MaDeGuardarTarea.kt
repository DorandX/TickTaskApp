package com.example.ticktask.manager

import android.app.Activity
import com.example.ticktask.manager.vista.IMaDeGuardarTarea
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.vista.viInterfaz.ViDeGuardarTarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MaDeGuardarTarea : IMaDeGuardarTarea {
    private var vista: ViDeGuardarTarea? = null
    private var dbManager: GestionDeDatos = GestionDeDatos().getInstance()

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


    override suspend fun actualizarTarea(
        idTarea: Int,
        idDeUsuario: MdUsuario,
        titulo: String,
        descripcion: String,
        prioridad: String,
        estado: String,
        entrega: Date
    ) {
        val noHayError = dbManager.actualizarDatosDeTarea(
            idTarea,
            idDeUsuario,
            titulo,
            descripcion,
            prioridad,
            estado,
            entrega
        )
        withContext(Dispatchers.Main) {
            if (noHayError) {
                vista?.actualizarTarea()
            } else {
                vista?.errorDeConexion()
            }
        }
    }

    override suspend fun guardarTareaEnMemoria(tarea: MdTarea) {
        val noHayError = dbManager.añadirTareaEnMemoria(tarea)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                //si no hay error al registral el usuario
                vista?.actualizarTarea()
            } else {
                vista?.errorDeConexion()
            }
        }
    }


}
