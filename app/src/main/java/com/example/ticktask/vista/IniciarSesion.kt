package com.example.ticktask.vista

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.databinding.VerIniciarSesionBinding
import com.example.ticktask.manager.ManagerDeInicio
import com.example.ticktask.manager.vista.IMaInicio
import com.example.ticktask.utilidades.Info
import com.example.ticktask.utilidades.Variables
import com.example.ticktask.vista.viInterfaz.ViDeInicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IniciarSesion : AppCompatActivity(), ViDeInicio {
    private lateinit var binding: VerIniciarSesionBinding
    private var gestion: IMaInicio = ManagerDeInicio()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = VerIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestion.entrarAVista(this)

        cargarVista()
    }

    private fun cargarVista() {
        iniciarSesion()
        conectarConMemoria()
        clickEn()
    }

    //Metodo que guarda la sesión del usuario.
    private fun iniciarSesion() {
        val preferencias = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val email = preferencias.getString("EMAIL", null)
        val clave = preferencias.getString("CLAVE", null)
        if (email != null && clave != null) {
            binding.EtqDeEmail.setText(email)
            binding.EdtClave.setText(clave)
        }
    }

    //Metodo que conecta con la BDD
    private fun conectarConMemoria() {
        cargarDatos(true)
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                gestion.getConexion()
            }
        }.invokeOnCompletion { cargarDatos(false) }
    }

    //Método que activa el progreso de carga de datos o muestra opciones registro, recuperar contraseña.
    private fun cargarDatos(mostrarCarga: Boolean) {
        if (mostrarCarga) {
            binding.cargandoInicio.visibility = View.VISIBLE
            binding.VerRegistrar.visibility = View.GONE
        } else {
            binding.cargandoInicio.visibility = View.GONE
            binding.VerRegistrar.visibility = View.VISIBLE
        }
    }

    private fun clickEn() {
        binding.BtnDeInicio.setOnClickListener {
            //Primero validemos que el usuario existe en la base de datos
            cargarDatos(true)
            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    gestion.esUnUsuarioValido(
                        binding.EtqDeEmail.text.toString(),
                        binding.EdtClave.text.toString()
                    )
                }
            }.invokeOnCompletion { cargarDatos(false) }
        }
        //Si no existe, habilitamos la opción de registrar
        binding.BtnDeRegistro.setOnClickListener {
            //Abrimos la actividad.
            startActivity(Intent(this, Registro::class.java))
            finish()
        }
    }

    override fun errorEnConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Info.errorDeConexion(this@IniciarSesion)
            }
        }.invokeOnCompletion { cargarDatos(false) }
    }

    override fun esUnUsuarioValido(idUsuario: Int) {
        val irATareas = Intent(this, Tareas::class.java)
        irATareas.putExtra("ID_DE_USUARIO", idUsuario)
        startActivity(irATareas)
        finish()
    }

    override fun esUnUsuarioNoValido() {
        Info.mostrarMensaje(this, "Usuario o contraseña incorrectos")
        cargarDatos(false)
    }
}