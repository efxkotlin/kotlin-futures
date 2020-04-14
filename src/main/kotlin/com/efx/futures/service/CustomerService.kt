package com.efx.futures.service

import com.efx.futures.domain.Customer
import com.efx.futures.repository.CustomerRepository
import java.util.*
import java.util.concurrent.CompletableFuture

class CustomerService constructor(private val customerRepository: CustomerRepository) {

    fun saveCustomer(customer: Customer):CompletableFuture<UUID> {
        return customerRepository.saveCustomer(customer).thenApply { customer.uuid }
    }

    fun getCustomerFirstName(uuid:UUID):CompletableFuture<String?> {
        return customerRepository.getCustomer(uuid).thenApply {
            customer -> customer?.firstName
        }
    }

    fun getCustomerLastName(uuid: UUID):CompletableFuture<String?> {
        return customerRepository.getCustomer(uuid).thenApply {
            customer -> customer?.lastName
        }
    }

    fun getCustomerZipCode(uuid: UUID):CompletableFuture<String?> {
        return customerRepository.getCustomer(uuid).thenApply {
            customer -> customer?.zipCode
        }
    }

    fun getCustomerEmail(uuid: UUID):CompletableFuture<String?> {
        return customerRepository.getCustomer(uuid).thenApply {
            customer -> customer?.email
        }
    }
}