package com.example.ticktask.vista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.databinding.VerIniciarSesionBinding
import com.example.ticktask.controlador.CntrlUsuario
import com.example.ticktask.controlador.itemEnMemoriaDeUsuario
import com.example.ticktask.memoria.TickTaskProvider
import com.example.ticktask.modelo.Usuario
import com.example.ticktask.vista.interfaz.InInicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException

class IniciarSesion : AppCompatActivity(), InInicio {
    private lateinit var inicio: VerIniciarSesionBinding
    private val controlador: CntrlUsuario by viewModels {
        itemEnMemoriaDeUsuario((application as TickTaskProvider).memoriaDeUsuario)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        inicio = VerIniciarSesionBinding.inflate(layoutInflater)
        setContentView(inicio.root)
        inicio.BtnDeInicio.setOnClickListener { iniciarSesion() }
    }

    private fun iniciarSesion() {
        val email = inicio.InEtqDeEmail.text.toString()
        val clave = inicio.InEtqDeClave.text.toString()
        val usuario = Usuario(0, email, clave)

        if (email.isNullOrEmpty() || clave.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, ingrese todos los datos.", Toast.LENGTH_SHORT).show()
            return
        } else {
            guardarUsuario()

        }
    }

    /**
    cargarInicio(true)
    controlador.validarUsuario(email, clave) { usuarioValido ->
    if (usuarioValido) {
    startActivity(Intent(this, Tareas::class.java))
    } else {
    cargarDatos(false)
    Toast.makeText(this, "Lo siento, usuario no encontrado.", Toast.LENGTH_SHORT).show()
    }
    }
    }
     */
    override fun salir() {
        finishActivity(0)
    }

    override fun errorDeConexion() {
        runOnUiThread {
            Toast.makeText(
                this@IniciarSesion,
                "Lo siento, usuario no encontrado.",
                Toast.LENGTH_SHORT
            ).show()
            cargarDatos(false)
        }
    }

    override fun conexionExitosa() {
        runOnUiThread {
            Toast.makeText(this@IniciarSesion, "Bienvenido de nuevo", Toast.LENGTH_SHORT)
                .show()
            cargarDatos(true)
        }
    }


    //Método que activa el progreso de carga de datos o muestra opciones registro, recuperar contraseña.
    private fun cargarDatos(mostrarCarga: Boolean) {
        if (mostrarCarga) {
            inicio.BtnDeInicio.visibility = View.VISIBLE
            inicio.BtnDeRegistro.visibility = View.GONE
        } else {
            inicio.BtnDeInicio.visibility = View.GONE
            inicio.BtnDeRegistro.visibility = View.VISIBLE
        }
    }

    private fun cargarInicio(mostrarCarga: Boolean) {
        if (mostrarCarga) {
            inicio.BtnDeInicio.visibility = View.GONE
            inicio.cargandoInicio.visibility = View.VISIBLE
        }
        inicio.BtnDeInicio.visibility = View.VISIBLE
        inicio.cargandoInicio.visibility = View.GONE
    }

    private fun cargarRegistro(mostrarCarga: Boolean) {
        if (mostrarCarga) {
            inicio.BtnDeRegistro.visibility = View.GONE
            inicio.BtnDeInicio.visibility = View.GONE
            inicio.cargandoInicio.visibility = View.VISIBLE
        }
        inicio.BtnDeRegistro.visibility = View.VISIBLE
        inicio.BtnDeInicio.visibility = View.GONE
        inicio.cargandoInicio.visibility = View.GONE
    }

    override fun guardarUsuario() {
        val email = inicio.InEtqDeEmail.text.toString()
        val clave = inicio.InEtqDeClave.text.toString()
        val usuario = Usuario(0, email, clave)
        if (email.isNullOrEmpty() && clave.isNullOrEmpty()) {
            runOnUiThread {
                Toast.makeText(
                    this@IniciarSesion,
                    "Faltan elementos por rellenar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            try {
                val usuario = Usuario(0, email, clave)
                lifecycleScope.launch(Dispatchers.IO) {
                    controlador.crearUsuario(usuario)
                }
                usuarioGuardado(usuario)
            } catch (ex: ParseException) {
                Toast.makeText(
                    this@IniciarSesion,
                    "Error, no se ha podido guardar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun usuarioGuardado(usuario: Usuario) {
        Toast.makeText(this@IniciarSesion, "Guardado exitosamente", Toast.LENGTH_SHORT)
            .show()
        val irATareas = Intent(this, Tareas::class.java)
        startActivity(irATareas)
        finish()
    }
}






