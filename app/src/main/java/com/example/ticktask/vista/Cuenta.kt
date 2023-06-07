package com.example.ticktask.vista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ticktask.databinding.VerCuentaBinding
import com.example.ticktask.manager.MaDeCuenta
import com.example.ticktask.manager.interfaz.IMaDeCuenta
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.utilidades.Info
import com.example.ticktask.vista.interfaz.ViDeCuenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Cuenta : AppCompatActivity(), ViDeCuenta {
    private lateinit var verCuenta: VerCuentaBinding
    private lateinit var usuario: MdUsuario
    private var controlador: IMaDeCuenta= MaDeCuenta()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        verCuenta= VerCuentaBinding.inflate(layoutInflater)
        setContentView(verCuenta.root)
        controlador.entrarAVista(this)
        verCuenta.BtnDeSalir.setOnClickListener {
            mostrarCargandoSalida(true)
            finish()
        }
        verCuenta.BtnDeBaja.setOnClickListener{
            darDeBajaUsuarioCorrectamente()
        }
        verCuenta.BtnDeCambiarClave.setOnClickListener(){
            actualizarContraseñaCorrectamente()
        }
    }


    override fun errorDeConexion() {
        lifecycleScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO){
                Info.errorDeConexion(this@Cuenta)
            }
        }.invokeOnCompletion { mostrarCargandoSalida (false)}

    }

    override fun darDeBajaUsuarioCorrectamente() {
        val uEmail = verCuenta.etxNombre.text.toString()
        val uClave = verCuenta.EdtsClave.text.toString()

        if (uEmail.isNotEmpty() && uClave.isNotEmpty()) {
            cargandoBaja(true)
            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    controlador.darDeBajaAUsuario(uEmail, uClave)
                    controlador.salirDeVista()
                }
            }.invokeOnCompletion { cargandoBaja(false) }
        } else {
            Toast.makeText(this, "Los datos son nulos o incorrectos, intentalo de nuevo", Toast.LENGTH_SHORT).show()
        }
    }


    override fun actualizarContraseñaCorrectamente() {
        var nClave= verCuenta.EdtNuevaClave.text.toString()
        var rClave= verCuenta.EdtRepetirNuevaClave.text.toString()
        verCuenta.BtnDeCambiarClave.setOnClickListener {
            mostrarCargandoNuevaClave(true)

            if(nClave.isNotEmpty()
                && rClave.isNotEmpty()
                && nClave.equals(rClave)){
                lifecycleScope.launch(Dispatchers.Main){
                    withContext(Dispatchers.IO){
                        controlador.actualizarContraseña(nClave,rClave)
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