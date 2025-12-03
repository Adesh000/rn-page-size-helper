package com.babacoder.rnpagesizehelper

data class LibraryResult(
    val name: String,
    val path: String,
    val isCompatible: Boolean,
    val pageSizeInElf: Long
)

data class ScanResult(
    val devicePageSize: Long,
    val isDevice16KB: Boolean,
    val abiFolders: List<String>,
    val scannedLibraries: List<LibraryResult>,
    val hasIncompatibleLibs: Boolean
)
