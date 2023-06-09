package com.example.ticktask.vista

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.R
import com.example.ticktask.databinding.VerGuardarTareaBinding
import com.example.ticktask.manager.MaDeGuardarTarea
import com.example.ticktask.manager.interfaz.IMaDeGuardarTarea
import com.example.ticktask.memoria.GestionDeDatos
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.utilidades.Info
import com.example.ticktask.vista.interfaz.ViDeGuardarTarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class GuardarTarea : AppCompatActivity(), ViDeGuardarTarea {
    private lateinit var guardarTarea: VerGuardarTareaBinding
    private var idTarea: Int = 0
    private var idDeUsuario: MdUsuario?=null
    private var controlador: IMaDeGuardarTarea = MaDeGuardarTarea()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        guardarTarea = VerGuardarTareaBinding.inflate(layoutInflater)
        setContentView(guardarTarea.root)
        controlador.entrarAVista(this)
        val btnFechaEntrega = guardarTarea.BtnDeEntregaDeTarea
        btnFechaEntrega.setOnClickListener {
            seleccionarFecha()
        }
        val btnDeGuardarTarea = guardarTarea.BtnDeSalvarTarea
        btnDeGuardarTarea.setOnClickListener {
            guardarTarea()
        }
        cargarVistas()

    }

    private fun cargarVistas() {
        seleccionarEstado()
        seleccionarPrioridad()
        seleccionarFecha()
    }

    private fun seleccionarPrioridad() {
        val adapter = ArrayAdapter(
            this,
            R.layout.ver_item_simple,
            resources.getStringArray(R.array.main_lista_de_prioridad)
        )
        guardarTarea.PrioridadDeTarea.setAdapter(adapter)
    }

    private fun seleccionarEstado() {
        val adapter = ArrayAdapter(
            this,
            R.layout.ver_item_simple,
            resources.getStringArray(R.array.iniciar_estado_de_tarea)
        )
        guardarTarea.EstadoDeTarea.setAdapter(adapter)
    }

    override fun errorDeConexion() {
        runOnUiThread {
            Toast.makeText(this@GuardarTarea, "Error de conexion", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Info.errorDeConexion(this@GuardarTarea, )
            }
        }.invokeOnCompletion { verProcesarDatos(false) }
    }

    override fun existeLaTarea() {
        Toast.makeText(this,"La tarea, ya exise", Toast.LENGTH_SHORT).show()
    }

    /**
     * Este método permite seleccionar la fecha al usuario
     * luego la adapta al formato normal
     * y la imprime en el botón.
     */
    private fun seleccionarFecha() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaSeleccionada = formatoFecha.format(selectedDate.time)

                // Asigna la fecha seleccionada al campo de texto
                guardarTarea.BtnDeEntregaDeTarea.text = fechaSeleccionada
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    /**
     * Este método hace una lectura de todos los datos ingresados en el layout de verGuardarTarea
     * Segun cada item, el texto y la selección se guardan en la Gestión de datos con el método de guardarTareaEnMemoria
     * Primero pasa por el filtro si existe, valida los datos y si están los datos y no coinciden con otra tarea, la guarda.
     */
    override fun guardarTarea() {
        val titulo = guardarTarea.TituloDeTarea.text.toString()
        val descripcion = guardarTarea.DescripcionDeNuevaTarea.text.toString()
        val prioridad = guardarTarea.PrioridadDeTarea.text.toString()
        val estado = guardarTarea.EstadoDeTarea.text.toString()

        // Obtener la fecha de entrega seleccionada
        val fechaEntregaText = guardarTarea.BtnDeEntregaDeTarea.text.toString()

        if (titulo.isNullOrEmpty() || estado.isNullOrEmpty() || fechaEntregaText.isNullOrEmpty()) {
            runOnUiThread {
                Info.mostrarMensaje(this, "Debe llenar los campos obligatorios")
            }
        } else {
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaEntrega = formatoFecha.parse(fechaEntregaText)

            verProcesarDatos(true)
            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                        MdTarea(
                            idTarea,
                            idDeUsuario!!.idUsuario,
                            titulo,
                            descripcion,
                            prioridad,
                            estado,
                            fechaEntrega as Date?
                        )
                    }
                }
            }
        }

    /**
     * Si el usuario edita la tarea, este actualiza los nuevos datos colocados y los guarda.
     */

    private fun verProcesarDatos(estaCargando: Boolean) {
        if (estaCargando) {
            guardarTarea.cargandoTarea.visibility = View.VISIBLE
            guardarTarea.BtnDeSalvarTarea.visibility = View.GONE
        } else {
            guardarTarea.BtnDeSalvarTarea.visibility = View.VISIBLE
            guardarTarea.cargandoTarea.visibility = View.GONE
        }
    }
}

