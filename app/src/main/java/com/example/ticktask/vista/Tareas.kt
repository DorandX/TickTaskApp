package com.example.ticktask.vista

import android.annotation.SuppressLint
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
import com.example.ticktask.databinding.VerItemDeTareaBinding
import com.example.ticktask.databinding.VerTareasBinding
import com.example.ticktask.manager.MaListaDeTareas
import com.example.ticktask.manager.interfaz.IMaListaDeTarea
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.utilidades.DeslizaYElimina
import com.example.ticktask.utilidades.DeslizarYEdita
import com.example.ticktask.utilidades.Variables
import com.example.ticktask.vista.interfaz.ViDeListaDeTareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList

class Tareas : AppCompatActivity(), ViDeListaDeTareas {
    //declaramos las variables de nuestras pantallas
    private lateinit var verLista: VerTareasBinding
    private lateinit var verTareas: VerItemDeTareaBinding
    private lateinit var idDeUsuario: MdUsuario
    private lateinit var tarea: MdTarea
    private var controlador: IMaListaDeTarea = MaListaDeTareas()


    //Validamos todas las variables que pertenecen a nuestro objeto tarea
    private var idTarea: Int = 0
    private var ldTareas = ArrayList<MdTarea>()
    private var ldTotalTareas = ArrayList<MdTarea>()
    private lateinit var adaptador: AdTarea



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        verLista = VerTareasBinding.inflate(layoutInflater)
        verTareas= VerItemDeTareaBinding.inflate(layoutInflater)
        setContentView(verLista.root)
        controlador.entrarAVista(this)
        val layoutManager = LinearLayoutManager(this)
        verLista.entradaItemsTareas.layoutManager = layoutManager
        adaptador= AdTarea(ldTareas,this,verTareas)
        mostrarTodasLasTareas(ldTareas)
        verLista.entradaItemsTareas.adapter= adaptador
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
        verLista.nuevaTarea.setOnClickListener {
            //Nos dirigiremos a la pantalla de nueva tarea
            CrearTarea(tarea).show(supportFragmentManager,"Nueva Tarea")
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
                Toast.makeText(this@Tareas, "Ningún elemento seleccionado", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        verLista.BtnVerPreferencias.setOnClickListener {
            val irACuenta = Intent(this, Cuenta::class.java)
            startActivity(irACuenta)
            finish()
        }
         ordenarTareasPorEstado()

        verLista.BtnOrdenAsc.setOnClickListener { ordenarEntrada() }
        verLista.BtnOrdenDesc.setOnClickListener{
            cargarOrdenDeTareas()
        }
        verLista.entradaItemsTareas.setOnClickListener {
            actualizarTarea(tarea)
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
                    idDeUsuario,
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
                idDeUsuario.let {
                    controlador.ordenarTareas(
                        idTarea, it, Variables.ITEM_TITULO,
                        verLista.BtnOrdenAsc.visibility == View.VISIBLE
                    )
                }
            }
        }.invokeOnCompletion {
            verCargarDatos(false)
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

    override fun actualizarTarea(tarea: MdTarea) {
        if (tarea.titulo.isEmpty() && tarea.estado.isEmpty() && tarea.entrega.toString().isEmpty()) {
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        } else {
            try {
                lifecycleScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        controlador.actualizarTarea(tarea)
                    }
                }
            } catch (ex: ParseException) {
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ordenarTareasPorEstado() {
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

        adaptador = AdTarea(ldTareas, this,verTareas)
        verLista.entradaItemsTareas.adapter = adaptador
        controlador.aplicarEstado(verLista.ordenPorEstado.selectedItemPosition, ldTotalTareas)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun aplicarFiltroEnVista(listaDeTareas: ArrayList<MdTarea>) {
        ldTareas.clear()
        ldTareas.addAll(listaDeTareas)
        adaptador.notifyDataSetChanged()
    }

    override fun errorDeConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Info.errorDeConexion(this@Tareas)
            }
        }.invokeOnCompletion { verCargarDatos(false) }
    }

    override fun mostrarTodasLasTareas(tareas: ArrayList<MdTarea>) {
        // limpia la lista de tareas actual
        ldTareas.clear()

        // agrega todas las nuevas tareas a la lista
        ldTareas.addAll(tareas)

        // notifica al adaptador que los datos han cambiado
        adaptador.notifyDataSetChanged()
    }
}