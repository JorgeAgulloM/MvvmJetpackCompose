package com.softyorch.mvvmjetpackcompose.core

import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import com.softyorch.mvvmjetpackcompose.utils.colorList
import com.softyorch.mvvmjetpackcompose.utils.deleteAccents
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

val phoneContacts = mutableListOf<ContactEntity>()

fun generateRandomContact(): ContactEntity {
    val random = java.util.Random()
    val id = UUID.randomUUID()
    val names = arrayOf("Jorge", "Juan", "María", "Carlos", "Laura", "Pedro", "Ana", "David", "Sofía", "Diego", "Elena")
    val name = names.random()
    val lastNames = arrayOf("Agulló", "Martín", "Pérez", "López", "Gómez", "Fernández", "Martínez", "Rodríguez", "Sánchez", "García", "Ruiz", "Torres")
    val lastName1 = lastNames.random()
    val lastName2 = lastNames.random()
    val age = random.nextInt(70) + 18 // Ages between 18 and 87
    val phoneNumber = (600000000 + random.nextInt(99999999)).toString() // Random numbers between 600000000 and 699999999
    val email = "${name.lowercase(Locale.ROOT).deleteAccents()}$age@${lastName1.lowercase(Locale.ROOT).deleteAccents()}.com"
    val typeCall = random.nextInt(4) // 0, 1, 2 or null (25% probability for each case)

    val colorList = colorList
    val color = colorList.random()

    val currentDate = Date()
    val lastYearDate = subtractYear(currentDate)
    val lastCall = Random.nextLong(lastYearDate.time, currentDate.time)
    val percent25 = Random.nextInt(4)
    val favorite = if (percent25 == 2) Random.nextBoolean() else false
    val blocked = if (!favorite && percent25 == 1) Random.nextBoolean() else false

    return ContactEntity(id, name, "$lastName1 $lastName2", phoneNumber, email, age, null, name[0].toString(), color, lastCall ,typeCall.takeIf { it < 3 }, favorite, blocked)
}

fun subtractYear(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.YEAR, -1)
    return calendar.time
}

fun generate(): MutableList<ContactEntity> {
    repeat(100) {
        val newContact = generateRandomContact()
        phoneContacts.add(newContact)
    }

    // Print the list of the first 10 phone contacts generated
    return phoneContacts
}



