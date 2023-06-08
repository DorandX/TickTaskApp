package com.example.ticktask.vista

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.R
import com.example.ticktask.databinding.BotonCrearTareaBinding
import com.example.ticktask.manager.MaDeCrearTarea
import com.example.ticktask.manager.interfaz.IMaDeCrearTarea
import com.example.ticktask.modelo.MdTarea
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [CrearTarea.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearTarea(var tarea: MdTarea?) : BottomSheetDialogFragment() {


    private var idTarea: Int = 0
    private var idDeUsuario: Int=0
    private var controlador: IMaDeCrearTarea = MaDeCrearTarea()
    private lateinit var usarBoton: BotonCrearTareaBinding


    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pantalla = requireActivity()
        if (tarea != null) {
            usarBoton.txNuevaTarea.text = "Tarea"
            val editar = Editable.Factory.getInstance()
            usarBoton.TituloDeTarea.text = editar.newEditable(tarea?.titulo)
            usarBoton.DescripcionDeNuevaTarea.text = editar.newEditable(tarea?.descripcion)
            usarBoton.EstadoDeTarea.text = editar.newEditable(tarea?.estado)
            usarBoton.BtnDeEntregaDeTarea.text = editar.newEditable(tarea?.entrega.toString())
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.boton_crear_tarea, container, false)
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

    fun errorDeConexion() {
        Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
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
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaSeleccionada = formatoFecha.format(selectedDate.time)

                // Asigna la fecha seleccionada al campo de texto
                usarBoton.BtnDeEntregaDeTarea.text = fechaSeleccionada
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    fun guardarTarea() {
        val titulo = usarBoton.TituloDeTarea.text.toString()
        val descripcion = usarBoton.DescripcionDeNuevaTarea.text.toString()
        val prioridad = usarBoton.PrioridadDeTarea.text.toString()
        val estado = usarBoton.EstadoDeTarea.text.toString()

        // Obtener la fecha de entrega seleccionada
        val fechaEntregaText = usarBoton.BtnDeEntregaDeTarea.text.toString()

        if (titulo.isNullOrEmpty() || estado.isNullOrEmpty() || fechaEntregaText.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaEntrega = formatoFecha.parse(fechaEntregaText)


                verProcesarDatos(true)
                val tarea = MdTarea(
                    idTarea,
                    idDeUsuario,
                    titulo,
                    descripcion,
                    prioridad,
                    estado,
                    fechaEntrega
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    controlador.guardarTareaEnMemoria(tarea)
                }
            } catch (ex: ParseException) {
                Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
                verProcesarDatos(false)
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
