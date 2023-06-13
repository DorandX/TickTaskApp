package com.example.ticktask.modelo

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.*
import com.example.ticktask.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "tarea",
    indices = [Index(value = ["idDeUsuario"])],
    foreignKeys = [ForeignKey(entity = Usuario::class, parentColumns = ["idDeUsuario"], childColumns = ["idDeUsuario"])]
)
class Tarea(
    @ColumnInfo(name = "titulo") var titulo: String,
    @ColumnInfo(name = "descripcion") var descripcion: String?,
    @ColumnInfo(name = "prioridad") var prioridad: String,
    @ColumnInfo(name = "estado") var estado: String,
    @ColumnInfo(name = "entrega") var entrega: String?,
    @PrimaryKey(autoGenerate = true) var idDeTarea: Int = 0,
    @ColumnInfo(name = "idDeUsuario") var idDeUsuario: Int = 0
){

    @RequiresApi(Build.VERSION_CODES.O)
    fun entrega(): LocalDate? = if (entrega==null) null
    else LocalDate.parse(entrega)
    @RequiresApi(Build.VERSION_CODES.O)
    fun tareaTerminada()= entrega() !=null
    @RequiresApi(Build.VERSION_CODES.O)
    fun ticTarea(): Int = if(tareaTerminada()) R.drawable.vc_tic else R.drawable.vc_icompleto
    @RequiresApi(Build.VERSION_CODES.O)
    fun colorTic(contexto:Context): Int = if(tareaTerminada()) terminada(contexto) else pendiente(contexto)

    private fun terminada(contexto: Context)= ContextCompat.getColor(contexto, R.color.verdecito)
    private fun pendiente(contexto: Context)= ContextCompat.getColor(contexto, R.color.black)
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        val formatoDeFecha: DateTimeFormatter= DateTimeFormatter.ISO_TIME
    }
}
