package com.example.ticktask.vista

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ticktask.adaptador.AdTarea
import com.example.ticktask.databinding.VerTareasBinding
import com.example.ticktask.controlador.CntrlTarea
import com.example.ticktask.controlador.itemEnMemoriaDeTarea
import com.example.ticktask.memoria.TickTaskProvider
import com.example.ticktask.modelo.Tarea
import com.example.ticktask.vista.interfaz.InTareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import com.example.ticktask.vista.CrearTarea as CrearTarea

class Tareas : AppCompatActivity(), InTareas {
    //declaramos las variables de nuestras pantallas
    private lateinit var entradaDeTareas: VerTareasBinding
    private val controlador: CntrlTarea by viewModels {
        itemEnMemoriaDeTarea((application as TickTaskProvider).memoriaDeTarea)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        entradaDeTareas = VerTareasBinding.inflate(layoutInflater)
        setContentView(entradaDeTareas.root)

        entradaDeTareas.nuevaTarea.setOnClickListener {
            //Nos dirigiremos a la pantalla de nueva tarea
            CrearTarea(null).show(supportFragmentManager, "Nueva Tarea")
        }
        listarTareas()

        entradaDeTareas.BtnVerPreferencias.setOnClickListener {
            val irACuenta = Intent(this, Cuenta::class.java)
            startActivity(irACuenta)
            finish()
        }

    }

    private fun listarTareas() {
        val lista = this
        controlador.listaDeTareas.observe(this){
            entradaDeTareas.entradaItemsTareas.apply {
                layoutManager= LinearLayoutManager(applicationContext)
                adapter= AdTarea(it,lista)
            }
        }
    }

    override fun actualizarTarea(tarea: Tarea) {
       controlador.actualizarTarea(tarea)
    }

    private fun verCargarDatos(estaCargando: Boolean) {
        if (estaCargando) {
            entradaDeTareas.cargaDatosDeTareas.visibility = View.VISIBLE
            entradaDeTareas.entradaItemsTareas.visibility = View.INVISIBLE
        } else {
            entradaDeTareas.cargaDatosDeTareas.visibility = View.GONE
            entradaDeTareas.entradaItemsTareas.visibility = View.VISIBLE
        }
    }


    override fun errorDeConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Toast.makeText(this@Tareas,"Error de conexion", Toast.LENGTH_SHORT)
                    .show()
            }

        }.invokeOnCompletion { verCargarDatos(false) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun completarTarea(tarea: Tarea) {
        controlador.completarTarea(tarea)
    }

    override fun eliminarTarea(tarea: Tarea) {
        controlador.eliminarTarea(tarea)
    }

}