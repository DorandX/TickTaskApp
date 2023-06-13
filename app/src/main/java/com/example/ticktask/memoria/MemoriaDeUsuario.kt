package com.example.ticktask.memoria

import androidx.annotation.WorkerThread
import com.example.ticktask.memoria.dao.UsuarioDao
import com.example.ticktask.modelo.Usuario

class MemoriaDeUsuario(private val iUsuarioDao: UsuarioDao) {

    @WorkerThread
    suspend fun validarUsuario(email: String, clave: String) {
    iUsuarioDao.validarUsuario(email, clave)
    }
    @WorkerThread
    suspend fun crearUsuario(usuario: Usuario) {
        iUsuarioDao.crearUsuario(usuario)
    }

    @WorkerThread
    suspend fun actualizarUsuario(usuario: Usuario) {
        iUsuarioDao.actualizarUsuario(usuario)
    }

    @WorkerThread
    suspend fun darDeBajaAUsuario(usuario: Usuario) {
        iUsuarioDao.darDeBajaAUsuario(usuario)
    }
}