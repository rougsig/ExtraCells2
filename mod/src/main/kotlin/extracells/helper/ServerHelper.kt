package extracells.helper

import net.minecraft.world.World


fun isClientWorld(world: World): Boolean {
  return world.isRemote
}

fun isServerWorld(world: World): Boolean {
  return !world.isRemote
}