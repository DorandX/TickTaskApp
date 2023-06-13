package com.example.ticktask.memoria

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ticktask.memoria.dao.TareaDao
import com.example.ticktask.memoria.dao.UsuarioDao
import com.example.ticktask.modelo.Tarea
import com.example.ticktask.modelo.Usuario

@Database(entities = [Tarea::class, Usuario::class], version=1, exportSchema=false)
abstract class TickTaskDB : RoomDatabase()
{
    abstract fun tareaDao():TareaDao
    abstract fun usuarioDao(): UsuarioDao
    companion object
    {
        @Volatile
        private var INSTANCE:TickTaskDB?=null
        fun conectarBd(contexto: Context):TickTaskDB
        {
            return INSTANCE?: synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    contexto.applicationContext, TickTaskDB::class.java,"tarea, usuario").build(
                )
                INSTANCE= instance
                instance
            }
        }
    }
}