package com.example.ticktask.vista.viInterfaz

import com.example.ticktask.modelo.MdTarea

interface ViDeListaDeTareas {
    fun aplicarFiltroEnVista(listaDeTareas: ArrayList<MdTarea>)
    fun aplicarOrdenEnVista(listaDeTareas: ArrayList<MdTarea>)
    fun actualizarTarea()
    fun errorDeConexion()
}