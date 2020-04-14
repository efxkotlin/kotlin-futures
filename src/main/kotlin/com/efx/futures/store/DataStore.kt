package com.efx.futures.store

import java.io.*
import java.nio.file.Path
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

interface ObjectStore {
    fun read(id: UUID): Any?
    fun write(id: UUID, obj: Any): Boolean
}

/**
 * File implementation of Object Store. All the data will be read and write to files.
 * It simulates a data store by providing blocking operations
 */
class FileStore constructor(fileStorePath: Path) : ObjectStore {

    private val readWriteLock = ReentrantReadWriteLock()
    private val fileStoreFolder: File = fileStorePath.toFile()

    init {
        assert(fileStoreFolder.exists())
        assert(fileStoreFolder.isDirectory)
    }

    override fun read(id: UUID): Any? {
        var result: Any? = null
        readWriteLock.readLock().withLock {
            val file = File(fileStoreFolder, id.toString())
            FileInputStream(file).use { fi ->
                ObjectInputStream(fi).use { oi ->
                    result = oi.readObject()
                }
            }
        }
        return result
    }

    override fun write(id: UUID, obj: Any): Boolean {
        var result = false
        val file = File(fileStoreFolder, id.toString())
        readWriteLock.writeLock().withLock {
            FileOutputStream(file).use { fo ->
                ObjectOutputStream(fo).use {
                    os -> os.writeObject(obj)
                    result = true
                }
            }
        }
        return result
    }

}