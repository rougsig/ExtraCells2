package extracells.extension

import net.minecraft.nbt.NBTTagCompound

internal val NBTTagCompound.keys
  get() = func_150296_c() as Set<String>

internal fun NBTTagCompound.toMap(): Map<String, Any> {
  val field = NBTTagCompound::class.java.getDeclaredField("tagMap")
  field.isAccessible = true
  return field.get(this) as Map<String, Any>
}
