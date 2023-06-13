package com.example.ticktask.vista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.databinding.VerRegistroBinding
import com.example.ticktask.controlador.CntrlUsuario
import com.example.ticktask.controlador.itemEnMemoriaDeUsuario
import com.example.ticktask.memoria.TickTaskProvider
import com.example.ticktask.modelo.Usuario
import com.example.ticktask.vista.interfaz.InUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException

class Registro : AppCompatActivity(), InUsuario{

    private lateinit var registro: VerRegistroBinding
    private var idUsuario: Int = 0
    private val controlador: CntrlUsuario by viewModels {
        itemEnMemoriaDeUsuario((application as TickTaskProvider).memoriaDeUsuario)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        registro = VerRegistroBinding.inflate(layoutInflater)
        setContentView(registro.root)
        registro.btnUnirme.setOnClickListener {
            cargarDatos(true)
            guardarUsuario()
        }
    }


    private fun cargarDatos(mostrarCarga: Boolean) {
        if (mostrarCarga) {
            registro.cargandoRegistro.visibility = View.VISIBLE
            registro.tvEstasListo.visibility = View.GONE
        } else {
            registro.cargandoRegistro.visibility = View.GONE
            registro.tvEstasListo.visibility = View.VISIBLE
        }
    }


    override fun conexionExitosa() {
        runOnUiThread {
            Toast.makeText(this@Registro, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show()
            cargarDatos(true)

        }    }

    override fun errorDeConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Toast.makeText(this@Registro, "Ningún elemento seleccionado", Toast.LENGTH_SHORT)
                    .show()
            }
        }.invokeOnCompletion { cargarDatos(false) }
    }


    override fun guardarUsuario() {
        val nombre = registro.txtNombre.text.toString()
        val apellido = registro.txApellido.text.toString()
        val email = registro.etxEmail.text.toString()
        val telefonoString = registro.etxMovil.text.toString()
        val clave = registro.etxClave.text.toString()

        if (email.isNullOrEmpty() && clave.isNullOrEmpty()) {
            runOnUiThread {
                Toast.makeText(this@Registro, "Faltan elementos por rellenar", Toast.LENGTH_SHORT).show()
            }
        } else {
            val telefono = telefonoString.toInt()
            if (telefono == null) {
                runOnUiThread {
                    Toast.makeText(this@Registro, "El número de teléfono no es válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                try {
                    val usuario = Usuario(idUsuario, email, clave)
                    lifecycleScope.launch(Dispatchers.IO) {
                        controlador.crearUsuario(usuario)
                    }
                    usuarioGuardado(usuario)
                } catch (ex: ParseException) {
                    Toast.makeText(this@Registro, "Error, no se ha podido guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun usuarioGuardado(usuario: Usuario) {
        Toast.makeText(this@Registro, "Guardado exitosamente", Toast.LENGTH_SHORT)
            .show()
        val irATareas = Intent(this, Tareas::class.java)
        startActivity(irATareas)
        finish()
    }
}
