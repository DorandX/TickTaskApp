package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.Tarea

interface InTareas {
    fun actualizarTarea(tarea: Tarea)
    fun errorDeConexion()
    fun completarTarea(tarea: Tarea)
    fun eliminarTarea(tarea: Tarea)
}