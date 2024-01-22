package com.softyorch.mvvmjetpackcompose.core

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import java.util.Calendar
import java.util.Date
import java.util.UUID
import kotlin.random.Random

val contactosTelefonicos = mutableListOf<UserEntity>()

fun generarUsuarioAleatorio(): UserEntity {
    val random = java.util.Random()
    val id = UUID.randomUUID()
    val nombres = arrayOf("Juan", "María", "Carlos", "Laura", "Pedro", "Ana", "David", "Sofía", "Diego", "Elena")
    val apellidos = arrayOf("Pérez", "López", "Gómez", "Fernández", "Martínez", "Rodríguez", "Sánchez", "García", "Ruiz", "Torres")
    val edades = random.nextInt(70) + 18 // Edades entre 18 y 87
    val phoneNumber = (600000000 + random.nextInt(99999999)).toString() // Números aleatorios entre 600000000 y 699999999
    val email = "${nombres.random().toLowerCase()}@example.com"
    val typeCall = random.nextInt(4) // 0, 1, 2 o null (25% de probabilidad para cada caso)

    val fechaActual = Date()
    val fechaHaceUnAno = restarAno(fechaActual)
    val lastCall = Random.nextLong(fechaHaceUnAno.time, fechaActual.time)

    return UserEntity(id, nombres.random(), apellidos.random(), phoneNumber, email, edades, lastCall ,typeCall.takeIf { it < 3 } )
}

fun restarAno(fecha: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = fecha
    calendar.add(Calendar.YEAR, -1)
    return calendar.time
}

fun generate(): MutableList<UserEntity> {
    repeat(100) {
        val nuevoUsuario = generarUsuarioAleatorio()
        contactosTelefonicos.add(nuevoUsuario)
    }

// Imprime la lista de los primeros 10 contactos telefónicos generados
    return contactosTelefonicos
}


