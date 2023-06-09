package com.example.ticktask.memoria

import java.sql.SQLException
import java.sql.Statement

object Datos {

    //Declaramos el nombre de la base de datos
    const val TICKTASK_BDD_NOMBRE= "TickTask"

    //Declaramos los nombres de las tablas
    const val TABLA_DE_USUARIO="Usuario"
    const val  TABLA_DE_TAREA="Tareas"

    //Declaramos los valores de las columnas

    //Tabla de usuario(1)
    const val ID_USUARIO="idUsuario"
    const val NOMBRE_DE_USUARIO= "nombre_de_usuario"
    const val APELLIDO_DE_USUARIO="apellido_de_usuario"
    const val EMAIL_DE_USUARIO="email_de_usuario"
    const val CLAVE_DE_USUARIO="clave_de_usuario"
    const val TELEFONO_DE_USUARIO="telefono_de_usuario"

    //Tabla de Tareas(2)
    const val ID_DE_TAREA="id_de_tarea"
    const val ID_DE_PROPIETARIO="id_de_propietario"
    const val TITULO_DE_TAREA="titulo_de_tarea"
    const val DESCRIPCION_DE_TAREA="descripcion_de_tarea"
    const val PROYECTO_DE_TAREA="proyecto_de_tarea"
    const val ESTADO_DE_TAREA= "estado_de_tarea"
    const val PRIORIDAD="prioridad_de_tarea"
    const val ENTREGA_DE_TAREA="entrega_de_tarea"

    const val CREAR_TABLA_DE_USUARIO = """"
            CREATE TABLE IF NOT EXISTS $TABLA_DE_USUARIO (
                    $ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT,  
                    $NOMBRE_DE_USUARIO TEXT NOT NULL, 
                    $APELLIDO_DE_USUARIO TEXT NOT NULL,
                    $EMAIL_DE_USUARIO TEXT NOT NULL,
                    $TELEFONO_DE_USUARIO INTEGER NOT NULL DEFAULT 0,
                    $CLAVE_DE_USUARIO TEXT NOT NULL
                    )
            """
    val CREAR_TABLA_DE_TAREA = """"
            CREATE TABLE IF NOT EXISTS $TABLA_DE_TAREA (
                    $ID_DE_TAREA INTEGER PRIMARY KEY AUTOINCREMENT, 
                    $ID_DE_PROPIETARIO INTEGER,
                    $TITULO_DE_TAREA TEXT NOT NULL, 
                    $DESCRIPCION_DE_TAREA TEXT, 
                    $PRIORIDAD TEXT NOT NULL, 
                    $ESTADO_DE_TAREA TEXT NOT NULL, 
                    $ENTREGA_DE_TAREA TEXT, 
                    FOREIGN KEY ($ID_DE_PROPIETARIO) REFERENCES $TABLA_DE_USUARIO($ID_USUARIO)
                    )
           """
    const val ELIMINAR_TABLA_DE_USUARIO = "DROP TABLE IF EXISTS $TABLA_DE_USUARIO"
    const val ELIMINAR_TABLA_DE_TAREA = "DROP TABLE IF EXISTS $TABLA_DE_TAREA"

}