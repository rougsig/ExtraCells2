package extracells.feature.part.fluidterminal.gui

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.core.entity.ECFluidStack
import extracells.extension.exhaustive
import extracells.feature.gui.container.ECGuiContainer
import extracells.feature.gui.widget.FluidWidget
import extracells.feature.part.fluidterminal.FluidTerminalPart
import extracells.feature.part.fluidterminal.netwotk.FluidTerminalClientPacket
import extracells.feature.part.fluidterminal.netwotk.FluidTerminalServerPacket
import extracells.network.ECNetworkHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.FluidRegistry
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
internal class FluidTerminalGui(
  private val terminal: FluidTerminalPart,
  private val player: EntityPlayer,
) : ECGuiContainer(FluidTerminalContainer(terminal, player)) {
  private val fluidWidgets = arrayListOf<FluidWidget>()

  init {
    for (i in 0..3) {
      for (j in 0..8) {
        val fluidWidget = FluidWidget(
          x = j * 18 + 7,
          y = i * 18 + 17,
          width = 18,
          height = 18,
        )
        fluidWidget.clickListener = { handleFluidWidgetClick(fluidWidget) }
        fluidWidgets.add(fluidWidget)
      }
    }
    this.fluidWidgets.forEach { addWidget(it) }
  }

  override fun drawBackground(alpha: Float, sizeX: Int, sizeY: Int) {
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    this.bindTexture("terminalfluid.png")
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 204)
  }

  override fun drawForeground(mouseX: Int, mouseY: Int) {
    for (widget in fluidWidgets) {
      if (widget.fluidStack?.name != null && terminal.selectedFluid?.name != null && widget.fluidStack?.name == terminal.selectedFluid?.name) {
        drawRect(
          widget.x,
          widget.y,
          widget.x + widget.width,
          widget.y + widget.height,
          0x7FFFFFFF,
        )
        break
      }
    }
  }

  private fun handleFluidWidgetClick(widget: FluidWidget) {
    val fluidName = widget.fluidStack?.name ?: return

    ECNetworkHandler.instance.sendToServer(
      FluidTerminalServerPacket.create(
        FluidTerminalServerPacket.Variant.UpdateSelectedFluid(
          fluidName = fluidName
        )
      )
    )
  }

  fun handleClientPacket(packet: FluidTerminalClientPacket) {
    when (val variant = packet.variant) {
      is FluidTerminalClientPacket.Variant.Empty -> Unit
      is FluidTerminalClientPacket.Variant.UpdateStoredFluids -> {
        fluidWidgets.forEachIndexed { index, fluidWidget ->
          fluidWidget.fluidStack = variant.fluids.getOrNull(index)?.let { fluid ->
            ECFluidStack(
              name = fluid.name,
              amount = fluid.amount,
            )
          }
        }
      }

      // TODO: that method executed only if gui opened
      //  fluid terminal part can be updated without opened gui (another player)
      //  Possible bug on sync client and server
      is FluidTerminalClientPacket.Variant.UpdateSelectedFluid -> {
        terminal.selectedFluid = FluidRegistry.getFluid(variant.fluidName)
      }
    }.exhaustive

  }
}
