package dev.kamko.bw_rs_backup.storage

import java.io.InputStream

interface CloudStorage {

    fun save(name: String, contentType: String, content: InputStream)
}
