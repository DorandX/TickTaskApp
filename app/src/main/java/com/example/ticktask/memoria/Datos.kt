package com.example.ticktask.memoria

object Datos {
    //Declaramos el nombre de la base de datos
    const val TICKTASK_BDD_NOMBRE= "blogandw_TickTask"

    //Declaramos los nombres de las tablas
    const val TABLA_DE_USUARIO="Usuario"
    const val  TABLA_DE_TAREA="Tareas"
    const val  TABLA_DE_PROYECTO="Proyectos"
    const val  TABLA_REGISTRO_DE_TAREA="Tarea_De_Usuario"

    //Declaramos los valores de las columnas

    //Tabla de usuario(1)
    const val ID_USUARIO="idUsuario"
    const val NOMBRE_DE_USUARIO= "nombre_de_usuario"
    const val APELLIDO_DE_USUARIO="apellido_de_usuario"
    const val EMAIL_DE_USUARIO="email_de_usuario"
    const val CLAVE_DE_USUARIO="clave_de_usuario"
    const val TELEFONO_DE_USUARIO="telefono_de_usuario"

    //Tabla de TareasDeUsuario(2)
    const val ID_TAREA_DE_USUARIO= "id_tarea_de_usuario"
    const val NOMBRE_DE_TAREA_DE_USUARIO="nombre_de_tarea_de_usuario"


    //Tabla de Tareas(2)
    const val ID_DE_TAREA="id_de_tarea"
    const val ID_DE_PROPIETARIO="id_de_propietario"
    const val TITULO_DE_TAREA="titulo_de_tarea"
    const val DESCRIPCION_DE_TAREA="descripcion_de_tarea"
    const val PROYECTO_DE_TAREA="proyecto_de_tarea"
    const val ESTADO_DE_TAREA= "estado_de_tarea"
    const val PRIORIDAD="prioridad_de_tarea"
    const val ENTREGA_DE_TAREA="entrega_de_tarea"

    //Tabla de Registro
    const val ID_DE_REGISTRO="id_registro"
    const val ID_TAREA_REGISTRADA ="id_tarea_registrada"


    //Tabla de Categoria (3)
    const val ID_DE_PROYECTO="id_de_proyecto"
    const val NOMBRE_DE_PROYECTO="nombre_de_proyecto"
    const val DESCRIPCION_DE_PROYECTO="descripcion_de_proyecto"
    const val ENTREGA_DE_PROYECTO="descripcion_de_proyecto"

}