@file:Suppress("SqlNoDataSourceInspection")

package dev.kamko.bw_rs_backup.bitwarden

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.EncryptionMethod
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.sql.DriverManager
import java.time.Instant

private val log = LoggerFactory.getLogger(BackupProvider::class.java)

class BackupProvider(
    private val password: String,
    private val dataFolder: Path = Path.of("/bw-data")
) {

    fun createZip(): InputStream {
        log.info("Generating ZIP")

        val target = Path.of(System.getProperty("java.io.tmpdir"), "bwbckp-${Instant.now()}.tmp")

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
}
