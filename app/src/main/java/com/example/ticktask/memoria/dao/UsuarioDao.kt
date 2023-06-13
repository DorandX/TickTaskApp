package com.example.ticktask.memoria.dao

import androidx.room.*
import com.example.ticktask.modelo.Usuario
import com.example.ticktask.vista.interfaz.InUsuario
import kotlin.Boolean

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario WHERE  email = :email AND clave = :clave")
    suspend fun validarUsuario(email: String, clave: String): Usuario

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun crearUsuario(usuario: Usuario)

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Delete
    suspend fun darDeBajaAUsuario(usuario: Usuario)
}