package com.babacoder.rnpagesizehelper

import android.content.pm.ApplicationInfo
import android.system.Os
import android.system.OsConstants
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.ReactMethod
import java.io.InputStream
import java.util.zip.ZipFile

@ReactModule(name = RnPageSizeHelperModule.NAME)
class RnPageSizeHelperModule(reactContext: ReactApplicationContext) :
  NativeRnPageSizeHelperSpec(reactContext) {

  override fun getName(): String = NAME

  @ReactMethod
override fun scan(promise: Promise) {
  try {
    val pageSize = Os.sysconf(OsConstants._SC_PAGESIZE)
    val isDevice16KB = pageSize >= 16384

    val appInfo: ApplicationInfo = reactApplicationContext.applicationInfo
    val apkPath = appInfo.sourceDir

    val zip = ZipFile(apkPath)
    val entries = zip.entries()

    val abiFolders = mutableSetOf<String>()
    val results = mutableListOf<LibraryResult>()

    while (entries.hasMoreElements()) {
      val entry = entries.nextElement()
      val name = entry.name

      if (name.endsWith(".so") && name.contains("lib/")) {
        val abi = name.split("/")[1]
        abiFolders.add(abi)

        val stream: InputStream = zip.getInputStream(entry)
        val align = ElfParser.getProgramHeaderAlignment(stream)
        stream.close()

        val compatible = align >= 16384

        results.add(
          LibraryResult(
            name = name.substringAfterLast("/"),
            path = name,
            isCompatible = compatible,
            pageSizeInElf = align
          )
        )
      }
    }

    val hasBad = results.any { !it.isCompatible }

    // Build abi array
    val abiArray = Arguments.createArray()
    abiFolders.forEach { abi ->
      abiArray.pushString(abi)
    }

    // Build scanned libraries array
    val libsArray = Arguments.createArray()
    results.forEach { lib ->
      val libMap = Arguments.createMap().apply {
        putString("name", lib.name)
        putString("path", lib.path)
        putBoolean("isCompatible", lib.isCompatible)
        putInt("pageSizeInElf", lib.pageSizeInElf.toInt())
      }
      libsArray.pushMap(libMap)
    }

    // Final return map
    val map = Arguments.createMap().apply {
      putInt("devicePageSize", pageSize.toInt())
      putBoolean("isDevice16KB", isDevice16KB)
      putArray("abiFolders", abiArray)
      putArray("scannedLibraries", libsArray)
      putBoolean("hasIncompatibleLibs", hasBad)
    }

    promise.resolve(map)
  } catch (e: Exception) {
    promise.reject("ERR_SCAN_FAILED", e)
  }
}


  companion object {
    const val NAME = "RnPageSizeHelper"
  }
}
