package com.example.ticktask.modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
class Usuario(
    @PrimaryKey(autoGenerate = true) var idDeUsuario: Int=0,
    @ColumnInfo(name="email") var email: String,
    @ColumnInfo(name="Clave")val clave: String
)

