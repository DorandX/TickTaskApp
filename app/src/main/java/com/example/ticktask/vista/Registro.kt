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
import com.example.ticktask.manager.vista.IMaRegistro
import com.example.ticktask.modelo.MdUsuario
import com.example.ticktask.utilidades.Info
import com.example.ticktask.vista.viInterfaz.ViDeRegistro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Registro : AppCompatActivity(), ViDeRegistro {

    private lateinit var binding: VerRegistroBinding
    private var idUsuario: Int = 0
    private var gestion: IMaRegistro= ManagerDeRegistro()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding= VerRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gestion.entrarAVista(this)
        cargarVistas()
    }

    private fun cargarVistas(){
        binding.btnUnirme.setOnClickListener{
            val nombre= binding.txtNombre.text.toString()
            val apellido= binding.txApellido.text.toString()
            val email = binding.etxEmail.text.toString()
            val telefono = binding.etxMovil.text.toString()
            val clave= binding.etxClave.text.toString()
            if(nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()
                || telefono.isEmpty() || clave.isEmpty()){
                Toast.makeText(this,"Por favor, rellena los campos",Toast.LENGTH_SHORT).show()
            } else{
                cargandoDatos(true)
                    lifecycleScope.launch(Dispatchers.Main){
                        withContext(Dispatchers.IO){
                            gestion.verificarSiExisteUsuario(email,clave)
                        }
                    } .invokeOnCompletion{cargandoDatos(false)}
                }
            }
        }//Aquí se podría cargar algo más

    private fun  cargandoDatos(mostrarCarga: Boolean){
        if(mostrarCarga){
            binding.cargandoRegistro.visibility= View.VISIBLE
            binding.tvEstasListo.visibility= View.GONE
        } else{
            binding.cargandoRegistro.visibility= View.GONE
            binding.tvEstasListo.visibility= View.VISIBLE
        }
    }
    override fun errorEnConexion() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Info.errorDeConexion(this@Registro)
            }
        }.invokeOnCompletion { cargandoDatos(false) }
    }

    override fun existeElUsuario() {
        Toast.makeText(this,"El usuario, ya exise", Toast.LENGTH_SHORT).show()
    }

    override fun guardarUsuario() {
        val nombre = binding.txtNombre.text.toString()
        val apellido = binding.txApellido.text.toString()
        val email = binding.etxEmail.text.toString()
        val telefono = binding.etxMovil.text.toString().toInt()
        val clave = binding.etxClave.text.toString()
        cargandoDatos(true)
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                gestion.agregarUsuario(MdUsuario(idUsuario, nombre, apellido, email, telefono, clave))
            }
        }.invokeOnCompletion { cargandoDatos(false) }
    }

    override fun usuarioAgregadoExitoso() {
        Info.mostrarMensaje(this,"Usuario agregado correctamente")
        val irATareas= Intent(this, Tareas::class.java)
        startActivity(irATareas)
        finish()
    }

    @Override
    override fun onDestroy() {
        gestion.salirDeVista()
        super.onDestroy()
    }
}
