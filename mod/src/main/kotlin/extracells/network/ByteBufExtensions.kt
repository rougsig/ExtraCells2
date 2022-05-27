package extracells.network

import io.netty.buffer.ByteBuf

fun ByteBuf.writeString(value: String): ByteBuf = apply {
  val ba = value.toByteArray()
  writeInt(ba.size)
  writeBytes(ba)
}

fun ByteBuf.readString(): String {
  val ba = ByteArray(readInt())
  readBytes(ba)
  return String(ba)
}
