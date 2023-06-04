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

class AdTarea(private var listaDeTareas: ArrayList<MdTarea>, var contexto: Context) :
    RecyclerView.Adapter<AdTarea.VerTodoLosDatos>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerTodoLosDatos {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemListBinding: VerItemDeTareaBinding =
            VerItemDeTareaBinding.inflate(layoutInflater,parent,false)
        return VerTodoLosDatos(itemListBinding)
    }
    class VerTodoLosDatos(var itemTarea: VerItemDeTareaBinding) :
        RecyclerView.ViewHolder(itemTarea.root) {

    }


    override fun onBindViewHolder(holder: VerTodoLosDatos, position: Int) {
        val tarea: MdTarea = listaDeTareas[position]
        holder.itemTarea.TituloDeTarea.text= tarea.titulo
        holder.itemTarea.DescripcionDeTarea.text= tarea.descripcion.toString()
        holder.itemTarea.Prioridad.setText(tarea.prioridad)
        holder.itemTarea.Estado.setText(tarea.estado)

        val formatoDeFecha= SimpleDateFormat("dd/MM/yyyy")
        val textoDeFecha= holder.itemTarea.BtnDeEntrega.text.toString()
        val fechaDeEntrega= formatoDeFecha.parse(textoDeFecha)
        if (fechaDeEntrega != null) {
            holder.itemTarea.BtnDeEntrega.text = fechaDeEntrega.toString()
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

    fun avisoDeCambio(cntx: Context, position: Int,idDeTarea: Int){
        val irAGuardarTarea = Intent(cntx, GuardarTarea::class.java)
        irAGuardarTarea.putExtra("ID_DE_TAREA", listaDeTareas[position].idDeTarea)
        startActivity(cntx,irAGuardarTarea,null)
    }
}