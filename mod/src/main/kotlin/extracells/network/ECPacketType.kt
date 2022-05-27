package extracells.network

import extracells.feature.part.fluidterminal.netwotk.FluidTerminalClientPacket
import extracells.feature.part.fluidterminal.netwotk.FluidTerminalServerPacket

enum class ECPacketType(val clazz: Class<out ECPacket>) {
  FluidTerminalServer(FluidTerminalServerPacket::class.java),
  FluidTerminalClient(FluidTerminalClientPacket::class.java),
}
