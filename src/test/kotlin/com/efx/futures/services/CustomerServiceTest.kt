package com.efx.futures.services

import com.efx.futures.domain.Customer
import com.efx.futures.repository.CacheSupportCustomerRepository
import com.efx.futures.repository.CustomerRepository
import com.efx.futures.service.CustomerService
import com.efx.futures.store.FileStore
import java.nio.file.FileSystems
import java.util.*
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomerServiceTest {

    private lateinit var customerRepository: CustomerRepository
    private lateinit var customerService: CustomerService

    @BeforeTest
    fun setup() {
        customerRepository = CacheSupportCustomerRepository(FileStore(FileSystems.getDefault().getPath(".")))
        customerService = CustomerService(customerRepository)
    }

    @Test
    fun addCustomer_shouldAddNewCustomerToRepo() {
        val customer = newCustomer {
            uuid = UUID.randomUUID()
            firstName = "First Name-${Random.nextInt(1, 10)}"
            lastName = "Second Name-${Random.nextInt(1, 10)}"
            zipCode = "Zip-${Random.nextInt(1, 10)}"
            email = "email-${Random.nextInt(1, 10)}@gmail.com"
        }
        val uuid = customerService.saveCustomer(customer).join()
        assertEquals(customer.uuid, uuid)
    }

    @Test
    fun addCustomer_andRetrieveCustomerFromRepo() {
        val customer = newCustomer {
            uuid = UUID.randomUUID()
            firstName = "First Name-${Random.nextInt(1, 10)}"
            lastName = "Second Name-${Random.nextInt(1, 10)}"
            zipCode = "Zip-${Random.nextInt(1, 10)}"
            email = "email-${Random.nextInt(1, 10)}@gmail.com"
        }
        val uuid = customerService.saveCustomer(customer).join()
        assertEquals(customer.uuid, uuid)

        val retrievedEmail = customerService.getCustomerEmail(uuid).join()
        assertEquals(customer.email, retrievedEmail)
    }
}

fun newCustomer(block:Customer.() -> Unit): Customer {
    val customer = Customer()
    block(customer)
    return customer
}