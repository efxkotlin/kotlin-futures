package com.efx.futures.repository

import com.efx.futures.domain.Customer
import com.efx.futures.store.ObjectStore
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

/**
 *  Repository provides the operations for Save and retrieval of customers from underlying data store
 */
interface CustomerRepository {
    fun saveCustomer(customer: Customer): CompletableFuture<Void>
    fun getCustomer(uuid: UUID): CompletableFuture<Customer?>
}

/**
 * This repository supports read and write to configured cache object as well, so that subsequent reads will be faster.
 */
class CacheSupportCustomerRepository(private val objectStore: ObjectStore) : CustomerRepository {

    //using a concurrent hashmap to store the customers against UUID
    private val concurrentCustomerCache = ConcurrentHashMap<UUID, Customer>()

    // read write lock to handle read and writes to the object store
    private val readWriteLock = ReentrantReadWriteLock()

    override fun saveCustomer(customer: Customer): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            readWriteLock.writeLock().withLock {
                objectStore.write(customer.uuid!!, customer)
                concurrentCustomerCache[customer.uuid!!] = customer
            }
        }
    }

    override fun getCustomer(uuid: UUID): CompletableFuture<Customer?> {
        var completableFuture:CompletableFuture<Customer?> = CompletableFuture()
        readWriteLock.readLock().withLock {
            completableFuture = if (concurrentCustomerCache.containsKey(uuid)) {
                CompletableFuture.completedFuture(concurrentCustomerCache[uuid])
            } else {
                CompletableFuture.supplyAsync {
                    objectStore.read(uuid) as Customer
                }
            }
        }
        return completableFuture
    }

}