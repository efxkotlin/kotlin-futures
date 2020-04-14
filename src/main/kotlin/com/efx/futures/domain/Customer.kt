package com.efx.futures.domain

import java.io.Serializable
import java.util.*

class Customer : Serializable {

    var uuid: UUID? = null
    var firstName: String = ""
    var lastName: String = ""
    var zipCode: String = ""
    var email: String = ""

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