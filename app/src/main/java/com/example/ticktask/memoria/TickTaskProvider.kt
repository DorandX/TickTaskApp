package com.example.ticktask.memoria

import android.app.Application


class TickTaskProvider: Application() {
   private val tickTaskDB by lazy{
       TickTaskDB.conectarBd(this)
   }
    val memoriaDeTarea by lazy{
        MemoriaDeTarea(tickTaskDB.tareaDao())
    }
    val memoriaDeUsuario by lazy{
        MemoriaDeUsuario(tickTaskDB.usuarioDao())
    }
}
