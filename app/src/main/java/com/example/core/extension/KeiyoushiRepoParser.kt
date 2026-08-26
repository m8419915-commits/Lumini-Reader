package com.example.core.extension

import com.example.domain.model.ExtensionPackage
import com.example.domain.model.ExtensionSourceMeta
import com.google.protobuf.CodedInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class KeiyoushiRepoParser(
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
) {

    suspend fun fetchRepoIndex(repoBaseUrl: String): Result<List<ExtensionPackage>> = withContext(Dispatchers.IO) {
        runCatching {
            val indexUrl = if (repoBaseUrl.endsWith("/")) "${repoBaseUrl}index.pb" else "$repoBaseUrl/index.pb"
            val request = Request.Builder()
                .url(indexUrl)
                .header("User-Agent", "LuminaReader/1.0 (Android)")
                .build()
            
            val response = httpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                throw IllegalStateException("Failed to load repo: HTTP ${response.code}")
            }

            val inputStream = response.body?.byteStream() ?: throw IllegalStateException("Empty repository stream")
            parseIndexProto(inputStream, repoBaseUrl)
        }
    }

    fun parseIndexProto(stream: InputStream, repoBaseUrl: String): List<ExtensionPackage> {
        val extensions = mutableListOf<ExtensionPackage>()
        val codedInput = CodedInputStream.newInstance(stream)

        try {
            while (!codedInput.isAtEnd) {
                val tag = codedInput.readTag()
                if (tag == 0) break

                val wireType = tag and 7
                val fieldNumber = tag ushr 3

                if (fieldNumber == 1 && wireType == 2) {
                    val length = codedInput.readRawVarint32()
                    val oldLimit = codedInput.pushLimit(length)
                    extensions.add(parseExtensionEntry(codedInput, repoBaseUrl))
                    codedInput.popLimit(oldLimit)
                } else {
                    codedInput.skipField(tag)
                }
            }
        } catch (e: Exception) {
            // Return any successfully parsed packages
        }
        return extensions
    }

    private fun parseExtensionEntry(input: CodedInputStream, repoBaseUrl: String): ExtensionPackage {
        var pkgName = ""
        var name = ""
        var versionCode = 0
        var versionName = ""
        var apkName = ""
        var iconUrl = ""
        var signatureHash = ""
        var lang = "all"
        var isNsfw = false
        val sources = mutableListOf<ExtensionSourceMeta>()

        while (!input.isAtEnd) {
            val tag = input.readTag()
            if (tag == 0) break
            when (tag ushr 3) {
                1 -> pkgName = input.readString()
                2 -> name = input.readString()
                3 -> versionCode = input.readInt32()
                4 -> versionName = input.readString()
                5 -> apkName = input.readString()
                6 -> iconUrl = input.readString()
                7 -> signatureHash = input.readString()
                8 -> lang = input.readString()
                9 -> isNsfw = input.readInt32() != 0
                10 -> {
                    val len = input.readRawVarint32()
                    val limit = input.pushLimit(len)
                    sources.add(parseSourceEntry(input))
                    input.popLimit(limit)
                }
                else -> input.skipField(tag)
            }
        }

        val cleanRepoUrl = repoBaseUrl.removeSuffix("/")
        val fullApkUrl = if (apkName.startsWith("http")) apkName else "$cleanRepoUrl/apk/$apkName"
        val fullIconUrl = if (iconUrl.startsWith("http")) iconUrl else "$cleanRepoUrl/icon/$iconUrl"

        return ExtensionPackage(
            packageName = pkgName,
            name = name,
            versionCode = versionCode,
            versionName = versionName,
            apkUrl = fullApkUrl,
            iconUrl = fullIconUrl,
            signatureHash = signatureHash,
            lang = lang,
            isNsfw = isNsfw,
            sources = sources
        )
    }

    private fun parseSourceEntry(input: CodedInputStream): ExtensionSourceMeta {
        var id = 0L
        var name = ""
        var lang = ""
        var baseUrl = ""
        var className = ""

        while (!input.isAtEnd) {
            val tag = input.readTag()
            if (tag == 0) break
            when (tag ushr 3) {
                1 -> id = input.readInt64()
                2 -> name = input.readString()
                3 -> lang = input.readString()
                4 -> baseUrl = input.readString()
                5 -> className = input.readString()
                else -> input.skipField(tag)
            }
        }
        return ExtensionSourceMeta(id, name, lang, baseUrl, className)
    }

    fun verifyApkSha256(apkBytes: ByteArray, expectedHash: String): Boolean {
        if (expectedHash.isEmpty()) return true
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(apkBytes)
        val hexString = hash.joinToString("") { "%02x".format(it) }
        return hexString.equals(expectedHash, ignoreCase = true)
    }
}
