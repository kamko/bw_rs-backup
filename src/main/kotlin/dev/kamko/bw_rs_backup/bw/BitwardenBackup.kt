@file:Suppress("SqlNoDataSourceInspection")

package dev.kamko.bw_rs_backup.bw

import dev.kamko.bw_rs_backup.storage.CloudStorage
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.sql.DriverManager
import java.time.LocalDateTime

class BitwardenBackup(
    private val storage: CloudStorage,
    private val password: String,
    private val dataFolder: Path = Path.of("/bw-data")
) {

    fun createBackup() {
        createZip(password).use {
            storage.save(filename(), "application/zip", it)
        }
    }

    private fun createZip(password: String): InputStream {
        val target = Files.createTempFile("bwbckp", ".zip")
        val zip = ZipFile(target.toFile())
        zip.setPassword(password.toCharArray())

        addDb(zip)
        addAttachments(zip)
        addIconCache(zip)

        val byteIs = ByteArrayInputStream(Files.readAllBytes(target))
        Files.deleteIfExists(target)

        return byteIs
    }

    private fun addDb(zip: ZipFile) {
        val connection = DriverManager.getConnection("jdbc:sqlite:${dataFolder}/db.sqlite3");
        val stm = connection.createStatement()

        val tmpFolder = Files.createTempDirectory("bwbckp")
        val target = tmpFolder.toAbsolutePath().resolve("db.sqlite3")

        stm.executeUpdate("backup to $target")

        zip.addFile(target.toFile(), zipParameters())

        Files.deleteIfExists(target)
        Files.deleteIfExists(tmpFolder)
    }

    private fun addAttachments(zip: ZipFile) = zip.addFolder(File(dataFolder.toFile(), "attachments"), zipParameters())

    private fun addIconCache(zip: ZipFile) = zip.addFolder(File(dataFolder.toFile(), "icon_cache"), zipParameters())

    private fun zipParameters(): ZipParameters {
        val zipParameters = ZipParameters();

        zipParameters.isEncryptFiles = true;
        zipParameters.encryptionMethod = EncryptionMethod.AES;
        zipParameters.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256;
        zipParameters.fileNameInZip

        return zipParameters
    }

    private fun filename() = "bitwarden_backup_${LocalDateTime.now()}-encrypted.zip"
}
