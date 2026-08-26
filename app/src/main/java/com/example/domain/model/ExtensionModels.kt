package com.example.domain.model

data class ExtensionPackage(
    val packageName: String,
    val name: String,
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val iconUrl: String,
    val signatureHash: String,
    val lang: String = "all",
    val isNsfw: Boolean = false,
    val isInstalled: Boolean = false,
    val sources: List<ExtensionSourceMeta> = emptyList()
)

data class ExtensionSourceMeta(
    val id: Long,
    val name: String,
    val lang: String,
    val baseUrl: String,
    val className: String
)
