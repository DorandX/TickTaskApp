package com.example.ticktask.manager.interfaz

import com.example.ticktask.manager.interfaz.vista.IMaVistas
import com.example.ticktask.modelo.MdTarea

interface IMaDeCrearTarea : IMaVistas{
    suspend fun validarTarea(tarea: MdTarea)
    suspend fun guardarTareaEnMemoria(tarea:MdTarea)

}