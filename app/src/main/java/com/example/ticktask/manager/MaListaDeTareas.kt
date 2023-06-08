package com.example.ticktask.manager

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ticktask.manager.interfaz.IMaListaDeTarea
import com.example.ticktask.memoria.AppContextProvider
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.utilidades.Variables
import com.example.ticktask.vista.interfaz.ViDeListaDeTareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class MaListaDeTareas : IMaListaDeTarea {
    private var vista: ViDeListaDeTareas? = null
    val dbManager: GestionDeDatos = GestionDeDatos.getInstance(AppContextProvider.getContext())

    override fun entrarAVista(view: Activity) {
        this.vista = view as ViDeListaDeTareas
    }

    override fun salirDeVista() {
        System.exit(0)
    }

    override fun aplicarEstado(filtro: Int, listaDeTareas: ArrayList<MdTarea>) {
        when (filtro) {
            Variables.ITEM_TODOS -> {
                //Si el filtro es todos,se muestran todas las tareas
                vista?.aplicarFiltroEnVista(listaDeTareas)
            }
            Variables.ITEM_EN_PROCESO -> {
                //Si el filtro es en proceso, se muestran las tareas que están en el lapso para realizar.
                val tareasEnProceso = ArrayList<MdTarea>()
                for (t in listaDeTareas.indices) {
                    if (listaDeTareas[t].estado == Variables.EN_PROCESO) {
                        tareasEnProceso.add(listaDeTareas[t])
                    }
                }
                vista?.aplicarFiltroEnVista(tareasEnProceso)
            }
            Variables.ITEM_PENDIENTE -> {
                //Si el filtro es pendiente se muestra solo las tareas pendientes por finalizar
                val tareasPendientes = ArrayList<MdTarea>()
                for (t in listaDeTareas.indices) {
                    if (listaDeTareas[t].estado == Variables.PENDIENTE) {
                        tareasPendientes.add(listaDeTareas[t])
                    }
                }
                vista?.aplicarFiltroEnVista(tareasPendientes)
            }
            Variables.ITEM_FINALIZADO -> {
                //Si el filtro en finalizado se muestran solo las tareas finalizadas
                val tareasFinalizadas = ArrayList<MdTarea>()
                for (t in listaDeTareas.indices) {
                    if (listaDeTareas[t].estado == Variables.FINALIZADO) {
                        tareasFinalizadas.add(listaDeTareas[t])
                    }
                }
                vista?.aplicarFiltroEnVista(tareasFinalizadas)
            }
        }
    }

    override suspend fun actualizarTarea(tarea: MdTarea) {
        val noHayError = dbManager.actualizarDatosDeTarea(tarea)
        withContext(Dispatchers.Main) {
            if (noHayError) {
                vista?.actualizarTarea(tarea)
            } else {
                vista?.errorDeConexion()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun ordenarTareas(
        idTarea: Int,
        idUsuario: MdUsuario,
        orderBy: Int,
        ascOrder: Boolean
    ) {
        var listaDeTareas: ArrayList<MdTarea>? = null
        when (orderBy) {
            Variables.ITEM_TITULO, 0 -> {
                //si el ordenamiento es por nombre, se obtiene la lista de tareas por titulo
                listaDeTareas =
                    dbManager.ordenarTareaSegunId(idTarea, idUsuario.idUsuario, "titulo", ascOrder)
            }
            Variables.ITEM_DESCRIPCION, 0 -> {
                //Si se ordena por descripción, se obtiene la lista de tareas por descripcion
                listaDeTareas = dbManager.ordenarTareaSegunId(
                    idTarea, idUsuario.idUsuario, "descripcion",
                    ascOrder
                )
            }
            Variables.ITEM_PRIORIDAD, 0 -> {
                //Si se ordena segun prioridad, se obtiene la lista de tarea segun prioridad
                listaDeTareas = dbManager.ordenarTareaSegunId(
                    idTarea, idUsuario.idUsuario, "prioridad",
                    ascOrder
                )
            }
            Variables.ITEM_ESTADO, 0 -> {
                //Si se ordena según estado, se obtiene la lista de tarea segun estado
                listaDeTareas =
                    dbManager.ordenarTareaSegunId(idTarea, idUsuario.idUsuario, "estado", ascOrder)
            }
            Variables.ITEM_ENTREGA, 0 -> {
                // Si se ordena segun fecha de entrega, se obtiene la lista, segun entrega.
                listaDeTareas = dbManager.ordenarTareaSegunId(
                    idTarea, idUsuario.idUsuario, "entrega",
                    ascOrder
                )
            }
        }
        withContext(Dispatchers.Main) {
            if (listaDeTareas == null) {
                vista?.errorDeConexion()
                return@withContext
            }
            vista?.aplicarOrdenEnVista(listaDeTareas)
        }
    }

    override suspend fun eliminarTarea(idTarea: Int) {
        val noHayError = dbManager.eliminarTarea(idTarea)
        withContext(Dispatchers.Main) {
            if (noHayError.not()) {
                vista?.errorDeConexion()
                return@withContext
            }
        }
    }
    override suspend fun obtenerTodasLasTareas() {
        val listaDeTareas = dbManager.mostrarTodasLasTareas()
        withContext(Dispatchers.Main) {
            if (listaDeTareas == null) {
                vista?.errorDeConexion()
                return@withContext
            }
            vista?.mostrarTodasLasTareas(listaDeTareas)
        }
    }
}