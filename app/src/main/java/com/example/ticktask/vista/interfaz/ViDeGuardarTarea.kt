package com.example.ticktask.vista.interfaz

import com.example.ticktask.modelo.MdTarea

interface ViDeGuardarTarea {
    fun errorDeConexion()
    fun existeLaTarea(tarea: MdTarea)
    fun guardarTarea()
    fun finalizarActividad()
    fun tareaGuardada(tarea: MdTarea)
}