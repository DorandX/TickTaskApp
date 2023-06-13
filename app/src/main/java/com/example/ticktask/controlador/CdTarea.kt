package com.example.ticktask.controlador

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.ticktask.memoria.MemoriaDeTarea
import com.example.ticktask.modelo.Tarea
import kotlinx.coroutines.launch
import java.time.LocalDate


class CntrlTarea(private val memoria: MemoriaDeTarea) : ViewModel() {

    var listaDeTareas: LiveData<List<Tarea>> = memoria.todasLasTareas.asLiveData()
    fun crearTarea(tarea: Tarea) = viewModelScope.launch {
        memoria.crearTarea(tarea)
    }

    fun actualizarTarea(tarea: Tarea) = viewModelScope.launch {
        memoria.actualizarTarea(tarea)
    }

    fun eliminarTarea(tarea: Tarea) = viewModelScope.launch {
        memoria.eliminarTarea(tarea)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun completarTarea(tarea: Tarea) = viewModelScope.launch {
        if (!tarea.tareaTerminada())
            tarea.entrega = Tarea.formatoDeFecha.format(LocalDate.now())
        memoria.actualizarTarea(tarea)
    }

}

class itemEnMemoriaDeTarea(private val memoria: MemoriaDeTarea) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CntrlTarea::class.java))
            return CntrlTarea(memoria) as T
        throw IllegalArgumentException("Se desconoce el controlador")
    }
}