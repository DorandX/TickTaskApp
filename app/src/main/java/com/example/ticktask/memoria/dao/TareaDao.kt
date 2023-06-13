package com.example.ticktask.memoria.dao

import androidx.room.*
import kotlin.collections.List
import com.example.ticktask.modelo.Tarea
import kotlinx.coroutines.flow.Flow


@Dao
interface TareaDao {
    @Query("SELECT * FROM tarea ORDER BY idDeTarea ASC")
    fun todasLasTareas(): Flow<List<Tarea>>

    @Insert
    suspend fun crearTarea(tarea: Tarea)

    @Update
    suspend fun actualizarTarea(tarea: Tarea)

    @Delete
    suspend fun eliminarTarea(tarea: Tarea)
}
