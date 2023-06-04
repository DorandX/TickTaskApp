package com.example.ticktask.manager.vista

import com.example.ticktask.manager.vista.utills.utilManager
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import java.util.*
import kotlin.collections.ArrayList

interface IMaListaDeTarea: utilManager {
    fun aplicarEstado(filtro:Int, nTareas: ArrayList<MdTarea>)
    suspend fun actualizarTarea(tarea: MdTarea)
    suspend fun ordenarTareas(idTarea: Int, orderBy: Int, ascIrder: Boolean)
    suspend fun eliminarTarea(idTarea: Int)
}