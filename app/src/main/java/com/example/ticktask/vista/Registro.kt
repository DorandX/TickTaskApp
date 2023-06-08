package com.example.ticktask.vista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.databinding.VerRegistroBinding
import com.example.ticktask.manager.ManagerDeRegistro
import com.example.ticktask.manager.interfaz.IMaRegistro
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.vista.interfaz.ViDeRegistro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException

class Registro : AppCompatActivity(), ViDeRegistro {

    private lateinit var binding: VerRegistroBinding
    private var idUsuario: Int = 0
    private var gestion: IMaRegistro = ManagerDeRegistro()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = VerRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestion.entrarAVista(this)
        val email = binding.etxEmail.text.toString()
        val clave = binding.etxClave.text.toString()
        binding.btnUnirme.setOnClickListener {
            if (validarCampos()) {
                cargandoDatos(true)
                lifecycleScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        gestion.verificarSiExisteUsuario(email, clave)
                    }
                }.invokeOnCompletion {
                    cargandoDatos(false)
                    guardarUsuario()
                }
            }
        }
    }

    private fun validarCampos(): Boolean {
        val nombre = binding.txtNombre.text.toString()
        val apellido = binding.txApellido.text.toString()
        val email = binding.etxEmail.text.toString()
        val telefono = binding.etxMovil.text.toString().toInt()
        val clave = binding.etxClave.text.toString()
        var camposValidos = true
        if (nombre.isEmpty() && apellido.isEmpty() && email.isEmpty() && telefono.toString()
                .isEmpty() && clave.isEmpty()
        ) {
            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT)
                .show()
            camposValidos = false
        }

        return camposValidos
    }

    private fun cargandoDatos(mostrarCarga: Boolean) {
        if (mostrarCarga) {
            binding.cargandoRegistro.visibility = View.VISIBLE
            binding.tvEstasListo.visibility = View.GONE
        } else {
            binding.cargandoRegistro.visibility = View.GONE
            binding.tvEstasListo.visibility = View.VISIBLE
        }
    }

    override fun errorEnConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Toast.makeText(this@Registro, "Ningún elemento seleccionado", Toast.LENGTH_SHORT)
                    .show()
            }
        }.invokeOnCompletion { cargandoDatos(false) }
    }

    override fun existeElUsuario(usuario: MdUsuario) {
        Toast.makeText(this, "El usuario, ya exise", Toast.LENGTH_SHORT).show()
    }


    override fun guardarUsuario() {
        val nombre = binding.txtNombre.text.toString()
        val apellido = binding.txApellido.text.toString()
        val email = binding.etxEmail.text.toString()
        val telefono = binding.etxMovil.text.toString().toInt()
        val clave = binding.etxClave.text.toString()

        if (nombre.isNullOrEmpty() || apellido.isNullOrEmpty() || email.isNullOrEmpty() || clave.isNullOrEmpty()) {
            runOnUiThread {
                Toast.makeText(this@Registro, "Faltan elementos por rellenar", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            try {
                val usuario = MdUsuario(
                    idUsuario,
                    nombre,
                    apellido,
                    email,
                    telefono,
                    clave
                )
                lifecycleScope.launch(Dispatchers.IO) {
                    gestion.agregarUsuario(usuario)
                }
                usuarioGuardado(usuario)
            } catch (ex: ParseException) {
                Toast.makeText(this@Registro, "Error, no se ha podido guardar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun usuarioGuardado(usuario: MdUsuario) {
        Toast.makeText(this@Registro, "Guardado exitosamente", Toast.LENGTH_SHORT)
            .show()
        val irATareas = Intent(this, Tareas::class.java)
        startActivity(irATareas)
        finish()
    }
}
