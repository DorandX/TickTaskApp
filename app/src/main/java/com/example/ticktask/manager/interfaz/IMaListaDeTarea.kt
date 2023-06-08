package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import kotlin.collections.ArrayList

interface IMaListaDeTarea: IMaVistas {
    fun aplicarEstado(filtro:Int, nTareas: ArrayList<MdTarea>)
    suspend fun actualizarTarea(tarea: MdTarea)
    suspend fun ordenarTareas(idTarea: Int, idUsuario: MdUsuario, orderBy: Int, ascIrder: Boolean)
    suspend fun eliminarTarea(idTarea: Int)
    suspend fun obtenerTodasLasTareas()
}