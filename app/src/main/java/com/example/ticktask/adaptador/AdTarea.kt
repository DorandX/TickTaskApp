package com.example.ticktask.adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktask.databinding.VerItemDeTareaBinding
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.vista.Tareas
import java.text.SimpleDateFormat

class AdTarea(
    private var listaDeTareas: MutableList<MdTarea>,
    private val contexto: Context,
    private val verItem: VerItemDeTareaBinding
) : RecyclerView.Adapter<AdTarea.AdItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdItem {
        val inflater = LayoutInflater.from(parent.context)
        val verItem = VerItemDeTareaBinding.inflate(inflater, parent, false)
        return AdItem(parent.context, verItem)
    }

    inner class AdItem(
        private val contexto: Context,
        private val verItem: VerItemDeTareaBinding
    ) : RecyclerView.ViewHolder(verItem.root) {
        init {
            verItem.root.setOnClickListener {
                avisoDeCambio(contexto, adapterPosition, listaDeTareas[adapterPosition].idDeTarea)
            }
        }

        fun verTodosLosItems(tarea: MdTarea) {
            verItem.TituloDeTarea.text = tarea.titulo
            verItem.DescripcionDeTarea.text = tarea.descripcion ?: "Descripción no disponible"
            verItem.Prioridad.setText(tarea.prioridad.toString())
            verItem.Estado.setText(tarea.estado.toString())
            verItem.BtnDeEntrega.text = SimpleDateFormat("dd/MM/yyyy").format(tarea.entrega)
        }
    }

    override fun onBindViewHolder(holder: AdItem, position: Int) {
        holder.verTodosLosItems(listaDeTareas[position])
    }

    override fun getItemCount(): Int = listaDeTareas.size

    fun eliminarDato(position: Int): Int {
        val idDato = listaDeTareas[position].idDeTarea
        listaDeTareas.removeAt(position)
        notifyItemRemoved(position)
        return idDato
    }

    fun avisoDeCambio(cntx: Context, position: Int, idDeTarea: Int) {
        val irAGuardarTarea = Intent(cntx, Tareas::class.java)
        irAGuardarTarea.putExtra("ID_DE_TAREA", idDeTarea)
        cntx.startActivity(irAGuardarTarea)
    }
}