package extracells.extension

import net.minecraft.nbt.NBTTagCompound

internal val NBTTagCompound.keys
  get() = func_150296_c() as Set<String>
