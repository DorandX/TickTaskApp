package com.example.ticktask.controlador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ticktask.memoria.MemoriaDeUsuario
import com.example.ticktask.modelo.Usuario
import kotlinx.coroutines.launch

class CntrlUsuario(private val memoria: MemoriaDeUsuario): ViewModel() {

    fun validarUsuario(email: String, clave: String) = viewModelScope.launch {
        memoria.validarUsuario(email, clave)
    }
    fun crearUsuario(usuario: Usuario) = viewModelScope.launch {
        memoria.crearUsuario(usuario)
    }
    /**
    fun registrarUsuario(usuario: Usuario) = viewModelScope.launch {
        memoria.registrarUsuario(usuario)
    }
*/
    fun actualizarUsuario(usuario: Usuario) = viewModelScope.launch {
        memoria.actualizarUsuario(usuario)
    }

    fun eliminarUsuario(usuario: Usuario) = viewModelScope.launch {
        memoria.darDeBajaAUsuario(usuario)
    }

}
    class itemEnMemoriaDeUsuario(private val memoria: MemoriaDeUsuario): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom((CntrlUsuario::class.java)))
                return CntrlUsuario(memoria) as T
            throw java.lang.IllegalArgumentException("Se desconoce el controlador")
        }
    }
