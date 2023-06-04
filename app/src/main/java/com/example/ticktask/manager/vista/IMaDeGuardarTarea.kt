package com.example.ticktask.manager.vista

import com.example.ticktask.manager.vista.utills.utilManager
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import java.util.*

interface IMaDeGuardarTarea : utilManager{
    suspend fun validarTarea(tarea: MdTarea)
    suspend fun guardarTareaEnMemoria(tarea:MdTarea)
}