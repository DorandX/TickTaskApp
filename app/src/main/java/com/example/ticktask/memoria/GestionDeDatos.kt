package com.example.ticktask.memoria

import com.example.ticktask.memoria.Datos.APELLIDO_DE_USUARIO
import com.example.ticktask.memoria.Datos.CLAVE_DE_USUARIO
import com.example.ticktask.memoria.Datos.DESCRIPCION_DE_TAREA
import com.example.ticktask.memoria.Datos.EMAIL_DE_USUARIO
import com.example.ticktask.memoria.Datos.ENTREGA_DE_TAREA
import com.example.ticktask.memoria.Datos.ESTADO_DE_TAREA
import com.example.ticktask.memoria.Datos.ID_DE_PROPIETARIO
import com.example.ticktask.memoria.Datos.ID_DE_TAREA
import com.example.ticktask.memoria.Datos.ID_USUARIO
import com.example.ticktask.memoria.Datos.TITULO_DE_TAREA
import com.example.ticktask.memoria.Datos.NOMBRE_DE_USUARIO
import com.example.ticktask.memoria.Datos.PRIORIDAD
import com.example.ticktask.memoria.Datos.PROYECTO_DE_TAREA
import com.example.ticktask.memoria.Datos.TABLA_DE_TAREA
import com.example.ticktask.memoria.Datos.TABLA_DE_USUARIO
import com.example.ticktask.memoria.Datos.TELEFONO_DE_USUARIO
import com.example.ticktask.memoria.Datos.TICKTASK_BDD_NOMBRE
import com.example.ticktask.modelo.MdTarea
import com.example.ticktask.modelo.MdUsuario
import java.sql.*
import java.util.ArrayList
import java.util.Date
import java.util.Properties

class GestionDeDatos {
    companion object {
        val instance = GestionDeDatos()
    }

    //Declaramos un singleton para no tener que hacer varias instancias
    fun getInstance(): GestionDeDatos {
        return instance
    }

    private var conexion: Connection? = null
    private var nUsuario = "blogandw_TickTas"
    private var uClave = "6T4jtATQgBcxv#G&njPh"


    //Configuramos la conexion
    fun getConnection(): Boolean {
        var noError = true //variable para controlar si hay error
        val datosDeConexion = Properties()// variable para guardar las propiedades de conexion
        datosDeConexion["usuario"] = nUsuario
        datosDeConexion["clave"] = uClave
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
            conexion = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                        "campoamor.me" + ":" +
                        "3306" + "/" +
                        TICKTASK_BDD_NOMBRE, datosDeConexion
            )
        } catch (ex: SQLException) {
            //Si hay error, lo guardaremos en la variable noError
            noError = false
            ex.printStackTrace()
        } catch (ex: Exception) {
            noError = false
            ex.printStackTrace()
        } finally {
            if (noError) {
                noError = executeMySQLQueryCreation()
            }
        }
        return noError
    }

    private fun executeMySQLQueryCreation(): Boolean {
        var stmt: Statement? = null // variable para ejecutar las consultas
        var noError = true // variable para controlar si hay error
        try {
            // intentamos ejecutar las consultas de las 3 tablas
            stmt = conexion?.createStatement()
            var query = ("CREATE TABLE IF NOT EXISTS " + TABLA_DE_USUARIO + "("
                    + ID_USUARIO + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + NOMBRE_DE_USUARIO + " varchar(45) NOT NULL, "
                    + APELLIDO_DE_USUARIO + " varchar(45) NOT NULL, "
                    + EMAIL_DE_USUARIO + " varchar(45) NOT NULL, "
                    + TELEFONO_DE_USUARIO + "INT NOT NULL DEFAULT 0, "
                    + CLAVE_DE_USUARIO + "varchar(45) NOT NULL,"
                    + ")")
            stmt?.executeQuery(query)

            query = ("CREATE TABLE IF NOT EXISTS " + TABLA_DE_TAREA + "("
                    + ID_DE_TAREA + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                    + ID_DE_PROPIETARIO + "INT NOT NULL"
                    + TITULO_DE_TAREA + " VARCHAR(45) NOT NULL, "
                    + DESCRIPCION_DE_TAREA + " VARCHAR(45) , "
                    + PRIORIDAD + " varchar(45) NOT NULL, "
                    + ESTADO_DE_TAREA + "VARCHAR(45) NOT NULL,"
                    + ENTREGA_DE_TAREA + "DATETIME," + "FOREIGN KEY (" + ID_DE_PROPIETARIO + ") REFERENCES " + TABLA_DE_USUARIO + "(" + ID_USUARIO + ")"
                    + ")")
            stmt?.executeQuery(query)

        } catch (ex: SQLException) {
            // si hay error, lo guardamos en la variable noError
            noError = false
            ex.printStackTrace()
        } finally {
            // al finalizar sin error, cerramos la conexion
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return noError
    }
    //Verificamos la reconexion a la base de datos

    fun tryReconnect(): Boolean {
        // probamos la conexion a la base de datos
        if (conexion == null || conexion?.isClosed == true) {
            // si no hay conexion, la intentamos
            for (i in 0..5) {
                if (getConnection()) return true
            }
        }
        return false
    }

    //CRUD USUARIO
    fun getUserId(nUsuario: String): Int? {
        var stmt: Statement? = null
        var resSet: ResultSet? = null
        var idDeUsuario: Int? = null
        try {
            stmt = conexion?.createStatement()
            val query =
                ("SELECT $ID_USUARIO FROM $TABLA_DE_USUARIO WHERE $NOMBRE_DE_USUARIO = '$nUsuario'")
            resSet = stmt?.executeQuery(query)

            if (resSet?.next() == true) {
                idDeUsuario = resSet.getInt(ID_USUARIO)
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
            if (resSet != null) {
                try {
                    resSet.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return idDeUsuario

    }

    fun esUnUsuarioValido(eUsuario: String, uClave: String): Boolean? {
        // comprobamos que username y password no esten vacios
        if (eUsuario.isEmpty() || uClave.isEmpty()) {
            return false
        }
        var esValido: Boolean? // variable para controlar si hay error
        var stmt: Statement? = null // variable para ejecutar las consultas
        var resQuery: ResultSet? = null // variable para guardar los resultados de las consultas

        try {
            // intentamos ejecutar la consulta
            stmt = conexion?.createStatement()
            val query = ("SELECT $EMAIL_DE_USUARIO FROM $TABLA_DE_USUARIO " +
                    "WHERE $EMAIL_DE_USUARIO = '$eUsuario' AND $CLAVE_DE_USUARIO = '$uClave'")
            resQuery = stmt?.executeQuery(query)

            esValido = resQuery?.next() // si hay resultados, isValid es true

        } catch (ex: SQLException) {
            // si hay error, lo guardamos en la variable isValid
            esValido = null
            ex.printStackTrace()
        } finally {
            // al finalizar sin error, cerramos la conexion
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
            if (resQuery != null) {
                try {
                    resQuery.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }

        return esValido
    }

    fun agregarUsuarioEnMemoria(nUsuario: MdUsuario): Boolean {
        var stmt: Statement? = null
        var noError = true
        try {
            stmt = conexion?.createStatement()
            val query =
                ("INSERT INTO $TABLA_DE_USUARIO ($NOMBRE_DE_USUARIO, $APELLIDO_DE_USUARIO, $EMAIL_DE_USUARIO, $TELEFONO_DE_USUARIO, $CLAVE_DE_USUARIO)" +
                        " VALUES ('${nUsuario.nombre}', '${nUsuario.apellido},'${nUsuario.email},'${nUsuario.clave}','${nUsuario.telefono},')")
            stmt?.executeQuery(query)

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

    fun verificarSiExisteUsuario(eUsuario: String, cUsuario: String): Boolean? {
        if (eUsuario.isEmpty() && cUsuario.isEmpty()) {
            return false
        }
        var esValido: Boolean?
        var stmt: Statement? = null
        var resSet: ResultSet? = null

        try {
            stmt = conexion?.createStatement()
            val query =
                ("SELECT $EMAIL_DE_USUARIO AND $CLAVE_DE_USUARIO FROM $TABLA_DE_USUARIO WHERE $EMAIL_DE_USUARIO= '$eUsuario' AND $CLAVE_DE_USUARIO ='$cUsuario' ")
            resSet = stmt?.executeQuery(query)
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

    // Metodo para actualizar los datos de un usuario
    fun actualizarDatosDeUsuario(usuEmail: String, usuClave: String): Boolean {
        var stmt: Statement? = null
        var noError = true
        try {
            stmt = conexion?.createStatement()
            val query =
                ("UPDATE ${TABLA_DE_USUARIO} SET $CLAVE_DE_USUARIO = '$usuClave' WHERE $EMAIL_DE_USUARIO = $usuEmail")

            stmt?.executeQuery(query)

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

    fun eliminarUsuario(uEmail: String, uClave: String): Boolean {
        var stmt: Statement? = null
        var noHayError = true
        try {
            stmt = conexion?.createStatement()
            // borramos el juego de la tabla intermedia de relacion
            var query =
                ("DELETE FROM $TABLA_DE_USUARIO WHERE $EMAIL_DE_USUARIO = $uEmail AND $CLAVE_DE_USUARIO = $uClave")
            stmt?.executeQuery(query)

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


    //CRUD TAREA
    fun añadirTareaEnMemoria(nTarea: MdTarea): Boolean {
        var stmt: Statement? = null
        var noError = true
        try {
            stmt = conexion?.createStatement()
            val query =
                ("INSERT INTO $TABLA_DE_TAREA ($ID_DE_TAREA, $ID_DE_PROPIETARIO,$TITULO_DE_TAREA, $DESCRIPCION_DE_TAREA $PRIORIDAD $ESTADO_DE_TAREA, $ENTREGA_DE_TAREA)" +
                        " VALUES ('${nTarea.idDeTarea}','${nTarea.idDeUsuario}','${nTarea.titulo}', '${nTarea.descripcion},'${nTarea.prioridad},'${nTarea.estado}','${nTarea.entrega},')")
            stmt?.executeQuery(query)

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

    fun verificarSiExisteTarea(nTarea: String): Boolean? {
        if (nTarea.isEmpty()) {
            return false
        }
        var esValido: Boolean?
        var stmt: Statement? = null
        var resSet: ResultSet? = null

        try {
            stmt = conexion?.createStatement()
            val query =
                ("SELECT $TITULO_DE_TAREA ROM $TABLA_DE_TAREA WHERE $TITULO_DE_TAREA= '$nTarea'")
            resSet = stmt?.executeQuery(query)
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


    // Metodo para obtener la lista de tareas según proyecto.
    fun ordenarTareaSegunId(
        idTarea: Int,
        idDeUsuario: MdUsuario,
        order: String? = null,
        ascOrder: Boolean = true
    ): ArrayList<MdTarea>? {
        var stmt: Statement? = null
        var resultSet: ResultSet? = null
        var tareas: ArrayList<MdTarea>? = null
        try {
            stmt = conexion?.createStatement()
            var query =
                ("SELECT $ID_DE_TAREA," +
                        "$ID_DE_PROPIETARIO" +
                        "$TITULO_DE_TAREA," +
                        "$DESCRIPCION_DE_TAREA," +
                        "$PROYECTO_DE_TAREA," +
                        "T1.$ESTADO_DE_TAREA," +
                        "T1.$ENTREGA_DE_TAREA," +
                        "FROM $TABLA_DE_TAREA WHERE T1.$ID_DE_TAREA = $idTarea")
            if (order != null) {
                val orderBy = if (ascOrder) "ASC" else "DESC"
                query += " ORDER BY $order $orderBy"
            }
            resultSet = stmt?.executeQuery(query)

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
                        resultSet.getDate(ENTREGA_DE_TAREA),

                        )
                )
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            if (stmt != null) {
                try {
                    stmt.close()
                } catch (sqlEx: SQLException) {
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close()
                } catch (sqlEx: SQLException) {
                }
            }
        }
        return tareas
    }

    // metodo para borrar un juego de un usuario
    fun eliminarTarea(idTarea: Int): Boolean {
        var stmt: Statement? = null
        var noHayError = true
        try {
            stmt = conexion?.createStatement()
            // borramos el juego de la tabla tareas
            var query = ("DELETE FROM $TABLA_DE_TAREA WHERE $ID_DE_TAREA = $idTarea")
            stmt?.executeQuery(query)

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


    fun actualizarDatosDeTarea(
    tarea: MdTarea
    ): Boolean {
        var stmt: Statement? = null
        var noHayError = true
        try {
            stmt = conexion?.createStatement()
            val query = ("UPDATE ${TABLA_DE_TAREA} SET $TITULO_DE_TAREA = '${tarea.titulo}'" +
                    " AND $DESCRIPCION_DE_TAREA = '${tarea.descripcion}'  AND $PRIORIDAD ='${tarea.prioridad}' " +
                    "AND $ESTADO_DE_TAREA ='${tarea.estado}'' AND $ENTREGA_DE_TAREA= '${tarea.entrega}''" +
                    " WHERE $ID_DE_TAREA = '${tarea.idDeTarea}' AND $ID_DE_PROPIETARIO = '${tarea.idDeUsuario}'")

            stmt?.executeQuery(query)

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

}
