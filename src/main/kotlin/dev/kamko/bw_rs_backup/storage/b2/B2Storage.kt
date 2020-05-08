package dev.kamko.bw_rs_backup.storage.b2

import com.backblaze.b2.client.B2StorageClientFactory
import dev.kamko.bw_rs_backup.USER_AGENT
import dev.kamko.bw_rs_backup.storage.CloudStorage
import java.io.InputStream

data class B2Key(val id: String, val value: String)

class B2Storage(appKey: B2Key, val bucket: String) : CloudStorage {

    private val client =  B2StorageClientFactory
        .createDefaultFactory()
        .create(appKey.id, appKey.value, USER_AGENT)


    override fun save(name: String, content: InputStream) {
    }
}
