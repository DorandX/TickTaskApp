package com.example.ticktask.vista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.databinding.VerCuentaBinding
import com.example.ticktask.controlador.CntrlUsuario
import com.example.ticktask.controlador.itemEnMemoriaDeUsuario
import com.example.ticktask.memoria.TickTaskProvider
import com.example.ticktask.modelo.Usuario
import com.example.ticktask.vista.interfaz.InCuenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Cuenta : AppCompatActivity(), InCuenta {
    private lateinit var verCuenta: VerCuentaBinding
    private val controlador: CntrlUsuario by viewModels {
        itemEnMemoriaDeUsuario((application as TickTaskProvider).memoriaDeUsuario)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        verCuenta= VerCuentaBinding.inflate(layoutInflater)
        setContentView(verCuenta.root)
        verCuenta.BtnDeSalir.setOnClickListener {
            mostrarCargandoSalida(true)
            salir()
        }
        verCuenta.BtnDeBaja.setOnClickListener{
            darDeBajaAUsuario()
        }
        verCuenta.BtnDeCambiarClave.setOnClickListener{
            actualizarContraseña()
            conexionExitosa()
        }
        verCuenta.BtnDeVolver.setOnClickListener{
            startActivity(Intent(this, Tareas::class.java))
            finish()
        }
    }


    override fun errorDeConexion() {
        lifecycleScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO){
                Toast.makeText(this@Cuenta, "Error de conexion", Toast.LENGTH_SHORT)
                    .show()
            }
        }.invokeOnCompletion { mostrarCargandoSalida (false)}

    }

    override fun conexionExitosa() {
        runOnUiThread {
            Toast.makeText(this@Cuenta, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show()

        }
    }


    override fun darDeBajaAUsuario() {
        val uEmail = verCuenta.etxNombre.text.toString()
        val uClave = verCuenta.EdtsClave.text.toString()
        val usuario = Usuario(0,uEmail,uClave)
        if (uEmail.isNotEmpty() && uClave.isNotEmpty()) {
            cargandoBaja(true)
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    controlador.eliminarUsuario(usuario)
                    withContext(Dispatchers.Main) {
                        // manejar la lógica de éxito aquí
                        salir()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Cuenta, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.invokeOnCompletion { cargandoBaja(false) }
        } else {
            Toast.makeText(this, "Los datos son nulos o incorrectos, intentalo de nuevo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun salir() {
       finishActivity(0)
    }

    override fun actualizarContraseña() {
        var email= verCuenta.etxEmail.text.toString()
        var nClave= verCuenta.EdtNuevaClave.text.toString()
        var rClave= verCuenta.EdtRepetirNuevaClave.text.toString()
        val usuario =Usuario(0,email,nClave)
        verCuenta.BtnDeCambiarClave.setOnClickListener {
            mostrarCargandoNuevaClave(true)

            if(nClave.isNotEmpty()
                && rClave.isNotEmpty()
                && nClave.equals(rClave)){
                lifecycleScope.launch(Dispatchers.Main){
                    withContext(Dispatchers.IO){
                        controlador.actualizarUsuario(usuario)
                    }
                }.invokeOnCompletion { mostrarCargandoNuevaClave(false) }
            }  else{
                Toast.makeText(this,"Error: Posiblemente los campos estén vacíos o no sea la misma clave", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarCargandoNuevaClave(mostrar: Boolean){
        if(mostrar){
            verCuenta.ProcesandoClave.visibility= View.VISIBLE
            verCuenta.ModificarClave.visibility= View.VISIBLE
            verCuenta.BtnDeBaja.visibility= View.GONE
        }else{
            verCuenta.ProcesandoClave.visibility= View.GONE
            verCuenta.ModificarClave.visibility= View.GONE
            verCuenta.BtnDeBaja.visibility= View.VISIBLE
        }
    }

    private fun mostrarCargandoSalida(mostrar:Boolean){
        if(mostrar){
            verCuenta.CargandoSalida.visibility= View.VISIBLE
            verCuenta.BtnDeSalir.visibility= View.GONE
        }else{
            verCuenta.BtnDeSalir.visibility=View.VISIBLE
            verCuenta.CargandoSalida.visibility=View.GONE
        }
    }

    private fun cargandoBaja(esClickeado: Boolean){
        if(esClickeado){
            verCuenta.CargandoProcesos.visibility= View.VISIBLE
            verCuenta.BtnDeCambiarClave.visibility= View.GONE
        } else {
            verCuenta.BtnDeCambiarClave.visibility= View.VISIBLE
            verCuenta.CargandoProcesos.visibility=View.GONE
        }
    }
}