package com.example.ticktask.vista

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktask.R
import com.example.ticktask.adaptador.AdTarea
import com.example.ticktask.databinding.VerGuardarTareaBinding
import com.example.ticktask.databinding.VerItemDeTareaBinding
import com.example.ticktask.databinding.VerTareasBinding
import com.example.ticktask.manager.MaListaDeTareas
import com.example.ticktask.manager.vista.IMaListaDeTarea
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.utilidades.DeslizaYElimina
import com.example.ticktask.utilidades.DeslizarYEdita
import com.example.ticktask.utilidades.Info
import com.example.ticktask.utilidades.Variables
import com.example.ticktask.vista.viInterfaz.ViDeListaDeTareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Tareas : AppCompatActivity(), ViDeListaDeTareas {
    //declaramos las variables de nuestras pantallas
    private lateinit var verLista: VerTareasBinding
    private lateinit var verTareas: VerItemDeTareaBinding
    private lateinit var crearTarea: VerGuardarTareaBinding
    private var idDeUsuario: MdUsuario?=null
    private var controlador: IMaListaDeTarea = MaListaDeTareas()

    //Validamos todas las variables que pertenecen a nuestro objeto tarea
    private var idTarea: Int = 0
    private var ldTareas = ArrayList<MdTarea>()
    private var ldTotalTareas = ArrayList<MdTarea>()
    private var adaptador = AdTarea(ldTareas, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        verLista = VerTareasBinding.inflate(layoutInflater)
        setContentView(verLista.root)
        controlador.entrarAVista(this)

        cargarVistas()

    }

    private fun cargarVistas() {
        cargarOrdenDeTareas()
        ordenarEntrada()
        cargarOrdenDeTareasPorEstado()
        clickEn()
        deslizar()

    }

    private fun clickEn() {
        verLista.ordenPorEstado.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    controlador.aplicarEstado(position, ldTareas)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        verLista.ordenTareas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                ordenarListaPorId(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        verLista.nuevaTarea.setOnClickListener {
            //Nos dirigiremos a la pantalla de nueva tarea
            val irACrearTarea = Intent(this, GuardarTarea::class.java)
            startActivity(irACrearTarea)
            finish()
        }
        verLista.BtnVerPreferencias.setOnClickListener {
            val irACuenta = Intent(this, Cuenta::class.java)
            startActivity(irACuenta)
            finish()
        }
    }


    //Metodo para configurar el deslizar para editar y/o borrar
    private fun deslizar() {
        val editaAlDeslizar = object : DeslizarYEdita(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = verLista.entradaItemsTareas.adapter as AdTarea
                adapter.avisoDeCambio(this@Tareas, viewHolder.adapterPosition, idTarea)
                finish()
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editaAlDeslizar)
        editItemTouchHelper.attachToRecyclerView(verLista.entradaItemsTareas)
        val eliminaAlDeslizar = object : DeslizaYElimina(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val psTarea = viewHolder.adapterPosition
                val msgTarea = AlertDialog.Builder(this@Tareas)
                    .setTitle("Eliminar entrada")
                    .setMessage("¿Quieres eliminar la siguiente tarea de la lista?")
                    .setNegativeButton("No") { view, _ ->
                        Toast.makeText(
                            this@Tareas,
                            "No ha eliminado la entrega",
                            Toast.LENGTH_SHORT
                        ).show()
                        view.dismiss()
                        verLista.entradaItemsTareas.layoutManager = LinearLayoutManager(
                            this@Tareas, LinearLayoutManager.VERTICAL, false
                        )
                        verLista.entradaItemsTareas.setHasFixedSize(true)
                    }
                    .setPositiveButton("Si") { view, _ ->
                        val idTarea = adaptador.eliminarDato(psTarea)
                        verCargarDatos(true)
                        lifecycleScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                controlador.eliminarTarea(idTarea)
                            }
                        }.invokeOnCompletion {
                            verCargarDatos(true)
                            Toast.makeText(this@Tareas, "Tarea eliminada", Toast.LENGTH_SHORT)
                                .show()
                        }
                        view.dismiss()
                    }
                    .setCancelable(false)
                    .create()
                msgTarea.show()
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(eliminaAlDeslizar)
        deleteItemTouchHelper.attachToRecyclerView(verLista.entradaItemsTareas)
    }

    private fun ordenarListaPorId(position: Int) {
        if (position == 0) {
            return
        }
        verCargarDatos(true)
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                controlador.ordenarTareas(
                    idTarea,
                    position,
                    verLista.BtnOrdenAsc.visibility == View.VISIBLE
                )
            }
        }.invokeOnCompletion { verCargarDatos(false) }
    }

    // Metodo para cargar la lista de juegos principal
    private fun ordenarEntrada() {
        verCargarDatos(true)
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                controlador.ordenarTareas(
                    idTarea, Variables.ITEM_TITULO,
                    verLista.BtnOrdenAsc.visibility == View.VISIBLE
                )
            }
        }.invokeOnCompletion {
            verCargarDatos(false)
            clickEn()
        }
    }

    private fun cargarOrdenDeTareas() {
        verLista.ordenTareas.adapter = object : ArrayAdapter<String>(
            this, R.layout.ver_item_simple,
            resources.getStringArray(R.array.main_lista_de_orden_de_tareas)
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val element = view as TextView
                if (position == 0) {
                    element.setTextColor(Color.BLUE)
                } else {
                    element.setTextColor(Color.BLACK)
                }
                return view
            }
        }
    }
    override fun actualizarTarea() {
        val titulo = verTareas.TituloDeTarea.text.toString()
        val descripcion = verTareas.DescripcionDeTarea.text.toString()
        val prioridad = verTareas.Prioridad.text.toString()
        val estado = verTareas.Estado.text.toString()

        // Obtener la fecha de entrega seleccionada
        val fechaEntregaText = verTareas.BtnDeEntrega.text.toString()

        if (titulo.isNullOrEmpty() || estado.isNullOrEmpty() || fechaEntregaText.isNullOrEmpty()) {
            runOnUiThread {
                Info.mostrarMensaje(this, "Debe llenar los campos obligatorios")
            }
        } else {
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaEntrega = formatoFecha.parse(fechaEntregaText)

                val tarea = MdTarea(idTarea, idDeUsuario!!, titulo, descripcion, prioridad, estado, fechaEntrega as Date?)

                lifecycleScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        controlador.actualizarTarea(tarea)
                    }
            }
        }
    }

    private fun cargarOrdenDeTareasPorEstado() {
        verLista.ordenPorEstado.adapter = object : ArrayAdapter<String>(
            this, R.layout.ver_item_simple,
            resources.getStringArray(R.array.iniciar_estado_de_tarea)
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val element = view as TextView
                if (position == 0) {
                    element.setTextColor(Color.BLUE)
                } else {
                    element.setTextColor(Color.BLACK)
                }
                return view
            }
        }
    }

    private fun verCargarDatos(estaCargando: Boolean) {
        if (estaCargando) {
            verLista.cargaDatosDeTareas.visibility = View.VISIBLE
            verLista.entradaItemsTareas.visibility = View.INVISIBLE
        } else {
            verLista.cargaDatosDeTareas.visibility = View.GONE
            verLista.entradaItemsTareas.visibility = View.VISIBLE
        }
    }

    override fun aplicarOrdenEnVista(listaDeTareas: ArrayList<MdTarea>) {
        verLista.entradaItemsTareas.setHasFixedSize(true)
        ldTareas.clear()
        ldTotalTareas.clear()
        ldTotalTareas.addAll(listaDeTareas)
        ldTareas.addAll(ldTotalTareas)

        adaptador = AdTarea(ldTareas, this)
        verLista.entradaItemsTareas.adapter = adaptador
        controlador.aplicarEstado(verLista.ordenPorEstado.selectedItemPosition, ldTotalTareas)
    }


    override fun aplicarFiltroEnVista(listaDeTareas: ArrayList<MdTarea>) {
        ldTareas.clear()
        ldTareas.addAll(listaDeTareas)
        adaptador?.notifyDataSetChanged()
    }

    override fun errorDeConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Info.errorDeConexion(this@Tareas)
            }
        }.invokeOnCompletion { verCargarDatos(false) }
    }

}