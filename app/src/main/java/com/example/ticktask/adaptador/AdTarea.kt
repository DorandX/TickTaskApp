package com.example.ticktask.adaptador

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktask.databinding.VerItemDeTareaBinding
import com.example.ticktask.modelo.Tarea
import com.example.ticktask.vista.interfaz.InTareas
import java.text.SimpleDateFormat

class AdTarea(
    private val listaDeTareas: List<Tarea>,
    private val onClick: InTareas
) : RecyclerView.Adapter<AdTarea.AdItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdItem {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VerItemDeTareaBinding.inflate(inflater, parent, false)
        return AdItem(parent.context, binding,onClick)
    }

    inner class AdItem(
        private val contexto: Context,
        private val binding: VerItemDeTareaBinding,
        private val onClick: InTareas
    ) : RecyclerView.ViewHolder(binding.root) {
       fun verItem(tarea: Tarea){
           if(tarea.tareaTerminada()){
               binding.TituloDeTarea.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
           }
           binding.TareaIncompleta.setImageResource(tarea.ticTarea())
          binding.TareaIncompleta.setColorFilter(tarea.colorTic(contexto))
           binding.TareaIncompleta.setOnClickListener{
               onClick.completarTarea(tarea)
           }
           binding.itemsTarea.setOnClickListener{
               onClick.actualizarTarea(tarea)
           }
           binding.BtnDeEliminar.setOnClickListener{
               onClick.eliminarTarea(tarea)
           }


       }

        fun verTodosLosItems(tarea: Tarea) {
            binding.TituloDeTarea.text = tarea.titulo
            binding.DescripcionDeTarea.text = tarea.descripcion ?: "Descripción no disponible"
            binding.Prioridad.setText(tarea.prioridad.toString())
            binding.Estado.setText(tarea.estado.toString())
            binding.BtnDeEntrega.text = SimpleDateFormat("dd/MM/yyyy").format(tarea.entrega)
        }
    }

    override fun onBindViewHolder(holder: AdItem, position: Int) {
        val tarea = listaDeTareas[position]
        holder.verTodosLosItems(tarea)
    }

    override fun getItemCount(): Int = listaDeTareas.size

    fun eliminarDato(position: Int): Int {
        val idDato = listaDeTareas[position].idDeTarea
        listaDeTareas.toMutableList().removeAt(position)
        notifyItemRemoved(position)
        return idDato
    }

}