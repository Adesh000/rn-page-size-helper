package com.babacoder.rnpagesizehelper

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ElfParser {

    fun getProgramHeaderAlignment(input: InputStream): Long {
        try {
            val header = ByteArray(64)
            input.read(header)

            val buffer = ByteBuffer.wrap(header)
            buffer.order(ByteOrder.LITTLE_ENDIAN)

            val eiClass = header[4].toInt()

            if (eiClass == 2) { // 64-bit ELF
                buffer.position(0x20) // e_phoff offset (64-bit)
                val phoff = buffer.long

                buffer.position(0x36) // e_phentsize
                val phentsize = buffer.short.toInt()

                buffer.position(0x38) // e_phnum
                val phnum = buffer.short.toInt()

                val phBytes = ByteArray(phentsize)
                input.skip(phoff - 64)
                input.read(phBytes)

                val phBuf = ByteBuffer.wrap(phBytes)
                phBuf.order(ByteOrder.LITTLE_ENDIAN)
                phBuf.position(0x20) // p_align offset

                return phBuf.long
            }

            return 4096
        } catch (e: Exception) {
            return 4096
        }
    }
}
