package com.example.ticktask.vista

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.ticktask.R
import com.example.ticktask.databinding.BotonCrearTareaBinding
import com.example.ticktask.controlador.CntrlTarea
import com.example.ticktask.modelo.Tarea
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class CrearTarea(var tarea: Tarea?) : BottomSheetDialogFragment() {
    private lateinit var usarBoton: BotonCrearTareaBinding
    private lateinit var controlador: CntrlTarea

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usarBoton = BotonCrearTareaBinding.inflate(inflater, container, false)
        return usarBoton.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val boton = requireActivity()
        if (tarea != null) {
            // Acciones para la creación de una nueva tarea
            usarBoton.txNuevaTarea.text =
                Editable.Factory.getInstance().newEditable("Nueva Tarea")
            val editar = Editable.Factory.getInstance()
            usarBoton.TituloDeTarea.text = editar.newEditable(tarea!!.titulo)
            usarBoton.DescripcionDeNuevaTarea.text = editar.newEditable(tarea!!.descripcion)
            usarBoton.EstadoDeTarea.text = editar.newEditable(tarea!!.estado)
            usarBoton.BtnDeEntregaDeTarea.text = editar.newEditable(tarea!!.entrega.toString())
        } else {
            usarBoton.TituloDeTarea.text =
                Editable.Factory.getInstance().newEditable("Nueva Tarea")
        }
        controlador = ViewModelProvider(boton).get(CntrlTarea::class.java)
        usarBoton.BtnDeSalvarTarea.setOnClickListener {
            guardarTarea()
        }
        val btnFechaEntrega = usarBoton.BtnDeEntregaDeTarea
        btnFechaEntrega.setOnClickListener {
            seleccionarFecha()
        }
        usarBoton.BtnDeSalvarTarea.setOnClickListener {
            guardarTarea()
        }
        usarBoton.EstadoDeTarea.setOnClickListener {
            seleccionarEstado()
        }
        usarBoton.BtnDeEntregaDeTarea.setOnClickListener {
            seleccionarFecha()
        }
        usarBoton.PrioridadDeTarea.setOnClickListener {
            seleccionarPrioridad()
        }
    }


    private fun seleccionarFecha() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Resto del código
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun seleccionarPrioridad() {
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.ver_item_simple,
            resources.getStringArray(R.array.main_lista_de_prioridad)
        )
        usarBoton.PrioridadDeTarea.setAdapter(adapter)
    }

    private fun seleccionarEstado() {
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.ver_item_simple,
            resources.getStringArray(R.array.iniciar_estado_de_tarea)
        )
        usarBoton.EstadoDeTarea.setAdapter(adapter)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun guardarTarea() {
        val titulo = usarBoton.TituloDeTarea.text.toString()
        val descripcion = usarBoton.DescripcionDeNuevaTarea.text.toString()
        val prioridad = usarBoton.PrioridadDeTarea.text.toString()
        val estado = usarBoton.EstadoDeTarea.text.toString()
        val entrega = usarBoton.BtnDeEntregaDeTarea.text.toString()
        val tarea = Tarea(titulo, descripcion,prioridad, estado, entrega)
        if (tarea.titulo.isEmpty() || tarea.descripcion.isNullOrEmpty() || tarea.prioridad.isNullOrEmpty() || tarea.estado.isNullOrEmpty() ) {
            Toast.makeText(
                requireActivity().applicationContext,
                "Por favor rellena todos los campos",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (this.tarea == null) {
                controlador.crearTarea(tarea)
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Tarea creada",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                tarea.idDeTarea = this.tarea!!.idDeUsuario
                controlador.actualizarTarea(tarea)
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Tarea actualizada",
                    Toast.LENGTH_LONG
                ).show()
            }
            dismiss()
        }
    }


    private fun verProcesarDatos(estaCargando: Boolean) {
        if (estaCargando) {
            usarBoton.cargandoTarea.visibility = View.VISIBLE
            usarBoton.BtnDeSalvarTarea.visibility = View.GONE
        } else {
            usarBoton.BtnDeSalvarTarea.visibility = View.VISIBLE
            usarBoton.cargandoTarea.visibility = View.GONE
        }
    }
}

