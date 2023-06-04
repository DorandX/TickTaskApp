package com.example.ticktask.memoria

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ticktask.memoria.Datos.APELLIDO_DE_USUARIO
import com.example.ticktask.memoria.Datos.CLAVE_DE_USUARIO
import com.example.ticktask.memoria.Datos.CREAR_TABLA_DE_TAREA
import com.example.ticktask.memoria.Datos.CREAR_TABLA_DE_USUARIO
import com.example.ticktask.memoria.Datos.DESCRIPCION_DE_TAREA
import com.example.ticktask.memoria.Datos.ELIMINAR_TABLA_DE_TAREA
import com.example.ticktask.memoria.Datos.ELIMINAR_TABLA_DE_USUARIO
import com.example.ticktask.memoria.Datos.EMAIL_DE_USUARIO
import com.example.ticktask.memoria.Datos.ENTREGA_DE_TAREA
import com.example.ticktask.memoria.Datos.ESTADO_DE_TAREA
import com.example.ticktask.memoria.Datos.ID_DE_PROPIETARIO
import com.example.ticktask.memoria.Datos.ID_DE_TAREA
import com.example.ticktask.memoria.Datos.ID_USUARIO
import com.example.ticktask.memoria.Datos.TITULO_DE_TAREA
import com.example.ticktask.memoria.Datos.NOMBRE_DE_USUARIO
import com.example.ticktask.memoria.Datos.PRIORIDAD
import com.example.ticktask.memoria.Datos.TABLA_DE_TAREA
import com.example.ticktask.memoria.Datos.TABLA_DE_USUARIO
import com.example.ticktask.memoria.Datos.TELEFONO_DE_USUARIO
import com.example.ticktask.memoria.Datos.TICKTASK_BDD_NOMBRE
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import java.sql.*
import java.util.ArrayList

class GestionDeDatos private constructor(contexto: Context): SQLiteOpenHelper(contexto,
    TICKTASK_BDD_NOMBRE,null,DATABASE_VERSION){
    companion object {
        const val TICKTASK_BDD_NOMBRE = "TickTaskDB"
        const val DATABASE_VERSION = 1

        private var instance: GestionDeDatos? = null

        fun getInstance(contexto: Context): GestionDeDatos {
            if (instance == null) {
                instance = GestionDeDatos(contexto.applicationContext)
            }
            return instance!!
        }
    }
override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(CREAR_TABLA_DE_USUARIO)
    db.execSQL(CREAR_TABLA_DE_TAREA)
}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(ELIMINAR_TABLA_DE_USUARIO)
        db!!.execSQL(ELIMINAR_TABLA_DE_TAREA)
    }


    private var conexion: Connection? = null



    //CRUD DEL OBJETO/ TABLA USUARIO

    /**
     * Este método valida que los campos ingresados en email y clave son correctos,
     * si los datos están correctos, se guarda en memoria, sino, salta un error.
     */
    fun validarUsuario(uEmail: String, uClave: String): Boolean? {
        // comprobamos que el nombre de usuario y la contraseña no estén vacíos
        if (uEmail.isEmpty() || uClave.isEmpty()) {
            return false
        }

        var esValido: Boolean? // variable para controlar si hay algun error
        var stmt: PreparedStatement? = null // variable para ejecutar las consultas preparadas
        var resQuery: ResultSet? = null // variable para guardar los resultados de las consultas

        try {
            // intentamos ejecutar la consulta preparada
            val query = "SELECT $EMAIL_DE_USUARIO FROM $TABLA_DE_USUARIO " +
                    "WHERE $EMAIL_DE_USUARIO = ? AND $CLAVE_DE_USUARIO = ?"
            stmt = conexion?.prepareStatement(query)
            stmt?.setString(1, uEmail)
            stmt?.setString(1, uClave)
            resQuery = stmt?.executeQuery()

            esValido = resQuery?.next() // si hay resultados, esValido es true

        } catch (ex: SQLException) {
            // si hay error, lo guardamos en la variable esValido
            esValido = null
            ex.printStackTrace()
        } finally {
            // al finalizar sin error, cerramos la conexión y liberamos los recursos
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                    sqlEx.printStackTrace()
                }
            }
            if (resQuery != null) {
                try {
                    resQuery.close()
                } catch (sqlEx: SQLException) {
                    sqlEx.printStackTrace()
                }
            }
        }

        return esValido
    }

    /**
     * Es importante agregar al usuario a la base de datos,
     * Para ello analisaremos el usuario ingresado y lo guardaremos en la memoria
     */
    fun guardarUsuario(usuario: MdUsuario): Boolean {
        var stmt: PreparedStatement? = null
        var noError = true
        try {
            val query =
                "INSERT INTO $TABLA_DE_USUARIO ($NOMBRE_DE_USUARIO, $APELLIDO_DE_USUARIO, $EMAIL_DE_USUARIO, $TELEFONO_DE_USUARIO, $CLAVE_DE_USUARIO) " +
                        "VALUES (?, ?, ?, ?, ?)"
            stmt = conexion?.prepareStatement(query)
            stmt?.setString(1, usuario.nombre)
            stmt?.setString(2, usuario.apellido)
            stmt?.setString(3, usuario.email)
            stmt?.setInt(4, usuario.telefono)
            stmt?.setString(5, usuario.clave)
            stmt?.executeUpdate()
        } catch (ex: SQLException) {
            noError = false
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                    sqlEx.printStackTrace()
                }
            }
        }
        return noError
    }

    /**
     * ¿Como creamos un usuario si ya existe anteriormente?
     * En este método se consulta la memoria para verificar si no existe,
     * de validar por email y clave que el usuario no existe, se guarda el usuario.
     * de no validar, muestra mensaje de error.
     */
    fun verificarUsuario(uEmail: String, uClave: String): Boolean? {
        if (uEmail.isEmpty() || uClave.isEmpty()) {
            return false
        }
        var esValido: Boolean?
        var stmt: PreparedStatement? = null
        var resSet: ResultSet? = null

        try {
            val query =
                "SELECT $EMAIL_DE_USUARIO, $CLAVE_DE_USUARIO FROM $TABLA_DE_USUARIO WHERE $EMAIL_DE_USUARIO = ? AND $CLAVE_DE_USUARIO = ?"
            stmt = conexion?.prepareStatement(query)
            stmt?.setString(1, uEmail)
            stmt?.setString(2, uClave)
            resSet = stmt?.executeQuery()
            esValido = resSet?.next() == true
        } catch (ex: SQLException) {
            esValido = null
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                    sqlEx.printStackTrace()
                }
            }
        }
        return esValido
    }

    /**
     * En cuenta, el usuario tiene la posibilidad de cambiar clave,
     * para ello actualizaremos los datos en memoria segun su email y la nueva clave.
     */
    fun actualizarUsuario(uEmail: String, uNuevaClave: String): Boolean {
        var noError = true
        var stmt: PreparedStatement? = null
        try {
            val query =
                "UPDATE $TABLA_DE_USUARIO SET $CLAVE_DE_USUARIO = ? WHERE $EMAIL_DE_USUARIO = ?"
            stmt = conexion?.prepareStatement(query)
            stmt?.setString(1, uNuevaClave)
            stmt?.setString(2, uEmail)
            stmt?.executeUpdate()
        } catch (ex: SQLException) {
            noError = false
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return noError
    }

    /**
     * El usuario tiene la opción de darse de baja desde su cuenta,
     * para ello, cuando el usuario no quiera esté interesado en la aplicación se dara la opción
     * y por ende al darle click en dar de baja, automáticamente se dara de baja en la memoria.
     */
    fun eliminarUsuario(uEmail: String, uClave: String): Boolean {
        var stmt: PreparedStatement? = null
        var noHayError = true
        try {
            val query =
                ("DELETE FROM $TABLA_DE_USUARIO WHERE $EMAIL_DE_USUARIO = ? AND $CLAVE_DE_USUARIO = ?")
            stmt = conexion?.prepareStatement(query)
            stmt?.setString(1, uEmail)
            stmt?.setString(2, uClave)
            stmt?.executeUpdate()

        } catch (ex: SQLException) {
            noHayError = false
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return noHayError
    }


    //CRUD PARA EL OBJETO/ TABLA TAREA

    /**
     * Para crear la tarea, solicitamos info de la tarea a publicar,
     * segun ello se añade a la memoria según los datos colocados en los campos.
     */
    fun crearTarea(nTarea: MdTarea): Boolean {
        var stmt: PreparedStatement? = null
        var noError = true
        try {

            val query =
                "INSERT INTO $TABLA_DE_TAREA ($ID_DE_TAREA, $ID_DE_PROPIETARIO, $TITULO_DE_TAREA, $DESCRIPCION_DE_TAREA, $PRIORIDAD, $ESTADO_DE_TAREA, $ENTREGA_DE_TAREA) VALUES (?, ?, ?, ?, ?, ?, ?)"
            stmt = conexion?.prepareStatement(query)
            stmt?.setInt(1, nTarea.idDeTarea)
            stmt?.setInt(2, nTarea.idDeUsuario)
            stmt?.setString(3, nTarea.titulo)
            stmt?.setString(4, nTarea.descripcion)
            stmt?.setString(5, nTarea.prioridad)
            stmt?.setString(6, nTarea.estado)
            stmt?.setDate(7, nTarea.entrega)
            stmt?.executeUpdate()
        } catch (ex: SQLException) {
            noError = false
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return noError
    }

    /**
     * A continuación validamos que existe la tarea,
     * inicializamos variable es valido a falso para que pueda contar si existe la tarea, sino retorna true.
     */
    fun verificarSiExisteTarea(nTarea: String): Boolean? {
        if (nTarea.isEmpty()) {
            return false
        }
        var esValido = false

        try {
            val query = "SELECT COUNT(*) FROM $TABLA_DE_TAREA WHERE $TITULO_DE_TAREA = ?"
            val stmt = conexion?.prepareStatement(query)
            stmt?.setString(1, nTarea)
            val resSet = stmt?.executeQuery()
            if (resSet?.next() == true) {
                val count = resSet.getInt(1)
                esValido = count > 0
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }

        return esValido
    }


    /**
     * si existe la tarea, pues ordenamos por id, y salen todos los datos.
     */
    fun ordenarTareaSegunId(
        idTarea: Int,
        idDeUsuario: Int,
        order: String? = null,
        ascOrder: Boolean = true
    ): ArrayList<MdTarea>? {
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        var tareas: ArrayList<MdTarea>? = null
        try {
            val query =
                ("SELECT $ID_DE_TAREA, $ID_DE_PROPIETARIO, $TITULO_DE_TAREA, $DESCRIPCION_DE_TAREA, $ESTADO_DE_TAREA, $ENTREGA_DE_TAREA " +
                        "FROM $TABLA_DE_TAREA WHERE $ID_DE_TAREA = ?")
            val orderBy = if (ascOrder) "ASC" else "DESC"
            val orderByClause = if (order != null) " ORDER BY $order $orderBy" else ""
            val finalQuery = query + orderByClause

            preparedStatement = conexion?.prepareStatement(finalQuery)
            preparedStatement?.setInt(1, idTarea)
            resultSet = preparedStatement?.executeQuery()

            tareas = ArrayList<MdTarea>()
            while (resultSet?.next() == true) {
                tareas.add(
                    MdTarea(
                        resultSet.getInt(ID_DE_TAREA),
                        idDeUsuario,
                        resultSet.getString(TITULO_DE_TAREA),
                        resultSet.getString(DESCRIPCION_DE_TAREA),
                        resultSet.getString(PRIORIDAD),
                        resultSet.getString(ESTADO_DE_TAREA),
                        resultSet.getDate(ENTREGA_DE_TAREA)
                    )
                )
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            preparedStatement?.close()
            resultSet?.close()
        }
        return tareas
    }

    /**
     * Si el usuario ha deslizado y quiere editar,
     * se realizan las actualizaciones para que se guarden luego en memoria.
     */
    fun actualizarDatosDeTarea(
        tarea: MdTarea
    ): Boolean {
        var preparedStatement: PreparedStatement? = null
        var noHayError = true
        try {
            val query = ("UPDATE $TABLA_DE_TAREA SET $TITULO_DE_TAREA = ?, " +
                    "$DESCRIPCION_DE_TAREA = ?, $PRIORIDAD = ?, " +
                    "$ESTADO_DE_TAREA = ?, $ENTREGA_DE_TAREA = ? " +
                    "WHERE $ID_DE_TAREA = ? AND $ID_DE_PROPIETARIO = ?")

            preparedStatement = conexion?.prepareStatement(query)
            preparedStatement?.setString(1, tarea.titulo)
            preparedStatement?.setString(2, tarea.descripcion)
            preparedStatement?.setString(3, tarea.prioridad)
            preparedStatement?.setString(4, tarea.estado)
            preparedStatement?.setDate(5, tarea.entrega)
            preparedStatement?.setInt(6, tarea.idDeTarea)
            preparedStatement?.setInt(7, tarea.idDeUsuario)
            preparedStatement?.executeUpdate()
        } catch (ex: SQLException) {
            noHayError = false
            ex.printStackTrace()
        } finally {
            preparedStatement?.close()
        }
        return noHayError
    }

    /**
     * Si el usuario desea eliminar, se llama a este método y la elimina de memoria.
     */
    fun eliminarTarea(idTarea: Int): Boolean {
        var pstmt: PreparedStatement? = null
        var noHayError = true
        try {
            val query = "DELETE FROM $TABLA_DE_TAREA WHERE $ID_DE_TAREA = ?"
            pstmt = conexion?.prepareStatement(query)
            pstmt?.setInt(1, idTarea)
            pstmt?.executeUpdate()
        } catch (ex: SQLException) {
            noHayError = false
            ex.printStackTrace()
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return noHayError
    }




}
