package com.example.ticktask.adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktask.databinding.VerItemDeTareaBinding
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.vista.GuardarTarea
import java.text.SimpleDateFormat

class AdTarea(
    private var listaDeTareas: ArrayList<MdTarea>,
    private val contexto: Context
) : RecyclerView.Adapter<AdTarea.VerTodoLosDatos>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerTodoLosDatos {
        val itemListBinding: VerItemDeTareaBinding =
            VerItemDeTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerTodoLosDatos(itemListBinding)
    }

    inner class VerTodoLosDatos(var itemTarea: VerItemDeTareaBinding) :
        RecyclerView.ViewHolder(itemTarea.root) {
        init {
            itemTarea.root.setOnClickListener {
                avisoDeCambio(contexto, adapterPosition, listaDeTareas[adapterPosition].idDeTarea)
            }
        }
    }

    override fun onBindViewHolder(holder: VerTodoLosDatos, position: Int) {
        val tarea: MdTarea = listaDeTareas[position]
        holder.itemTarea.apply {
            TituloDeTarea.text = tarea.titulo
            DescripcionDeTarea.text = tarea.descripcion ?: "Descripción no disponible"
            Prioridad.setText(tarea.prioridad)
            Estado.setText(tarea.estado)
            BtnDeEntrega.text = SimpleDateFormat("dd/MM/yyyy").format(tarea.entrega!!)
        }
    }

    override fun getItemCount(): Int {
        return listaDeTareas.size
    }

    fun eliminarDato(position: Int): Int {
        val idDato = listaDeTareas[position].idDeTarea
        listaDeTareas.removeAt(position)
        notifyItemRemoved(position)
        return idDato
    }

    fun avisoDeCambio(cntx: Context, position: Int, idDeTarea: Int) {
        val irAGuardarTarea = Intent(cntx, GuardarTarea::class.java)
        irAGuardarTarea.putExtra("ID_DE_TAREA", idDeTarea)
        cntx.startActivity(irAGuardarTarea)
    }
}