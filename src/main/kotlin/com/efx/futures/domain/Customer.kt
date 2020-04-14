package com.efx.futures.domain

import java.util.*

class Customer(
    var uuid: UUID,
    val firstName:String,
    val lastName:String,
    val contactNumber:String,
    val zipCode:String,
    val email:String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer

        if (uuid != other.uuid) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}