package com.example.ticktask.memoria

import androidx.annotation.WorkerThread
import com.example.ticktask.memoria.dao.TareaDao
import com.example.ticktask.modelo.Tarea
import kotlinx.coroutines.flow.Flow

class MemoriaDeTarea(private val iTareaDao: TareaDao) {
    val todasLasTareas : Flow<List<Tarea>> =iTareaDao.todasLasTareas()

    @WorkerThread
    suspend fun crearTarea(tarea: Tarea) {
        iTareaDao.crearTarea(tarea)
    }

    @WorkerThread
    suspend fun actualizarTarea(tarea: Tarea) {
        iTareaDao.actualizarTarea(tarea)
    }

    @WorkerThread
    suspend fun eliminarTarea(tarea: Tarea) {
        iTareaDao.eliminarTarea(tarea)
    }
}