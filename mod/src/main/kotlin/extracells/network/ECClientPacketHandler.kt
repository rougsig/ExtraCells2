package extracells.network

import cpw.mods.fml.common.network.internal.FMLProxyPacket
import net.minecraft.entity.player.EntityPlayer

internal interface ECClientPacketHandler {
  fun onPacketData(packet: FMLProxyPacket, player: EntityPlayer?)
}
