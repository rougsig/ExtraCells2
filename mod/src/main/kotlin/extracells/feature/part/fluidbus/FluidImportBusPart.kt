package extracells.feature.part.fluidbus

import appeng.api.AEApi
import appeng.api.config.Actionable
import appeng.api.config.FuzzyMode
import appeng.api.config.RedstoneMode
import appeng.api.config.Settings
import appeng.api.networking.IGridNode
import appeng.api.networking.security.MachineSource
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartRenderHelper
import appeng.client.texture.CableBusTextures
import appeng.core.settings.TickRates
import appeng.core.sync.GuiBridge
import appeng.me.GridAccessException
import appeng.util.Platform
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import extracells.feature.part.ECPart
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.Vec3
import net.minecraftforge.fluids.FluidContainerRegistry

internal class FluidImportBusPart : PartSharedFluidBus(ECPart.FluidImportBus.meta) {
  private val mySrc = MachineSource(this)

  init {
    this.configManager.registerSetting(Settings.REDSTONE_CONTROLLED, RedstoneMode.IGNORE)

    // ignore it. that upgrade can't be installed. needed just for appeng gui support
    this.configManager.registerSetting(Settings.FUZZY_MODE, FuzzyMode.IGNORE_ALL)
  }

  override fun doBusWork(): TickRateModulation {
    if (!proxy.isActive || !canDoBusWork()) return TickRateModulation.IDLE

    var worked = false

    try {
      val dest = this.fluidHandler
      val inv = proxy.storage.fluidInventory

      // TODO: send energy on fluid extract
      //  val energy = proxy.energy

      if (dest == null) return TickRateModulation.SLEEP

      var fluidToSend = calculateFluidToSend
      for (fluid in config) {
        val fluidStack = FluidContainerRegistry.getFluidForFilledItem(fluid)
          ?.apply { amount = fluidToSend }

        val maxAmountToSend = if (fluidStack != null) dest.drain(side.opposite, fluidStack, false)
        else dest.drain(side.opposite, fluidToSend, false)

        if (maxAmountToSend != null) {
          val aeFluidStack = AEApi.instance().storage().createFluidStack(maxAmountToSend)
          val notInjectedAmount = inv.injectItems(aeFluidStack, Actionable.MODULATE, mySrc)
          if (notInjectedAmount != null) {
            val realAmountToSend = maxAmountToSend.amount - notInjectedAmount.fluidStack.amount
            dest.drain(side.opposite, realAmountToSend, true)
            fluidToSend -= realAmountToSend
            worked = true
          }
        }

        if (fluidToSend <= 0) break
      }

    } catch (e: GridAccessException) {
      // :P
    }

    return if (worked) TickRateModulation.FASTER else TickRateModulation.SLOWER
  }

  // region render
  override fun onPartActivate(player: EntityPlayer, pos: Vec3?): Boolean {
    if (player.isSneaking) return false
    if (!Platform.isClient()) Platform.openGUI(player, host.tile, side, GuiBridge.GUI_BUS)
    return true
  }

  override fun getBoxes(bch: IPartCollisionHelper) {
    bch.addBox(6.0, 6.0, 11.0, 10.0, 10.0, 13.0)
    bch.addBox(5.0, 5.0, 13.0, 11.0, 11.0, 14.0)
    bch.addBox(4.0, 4.0, 14.0, 12.0, 12.0, 16.0)
  }

  @SideOnly(Side.CLIENT)
  override fun renderInventory(rh: IPartRenderHelper, renderer: RenderBlocks?) {
    rh.setTexture(
      CableBusTextures.PartImportSides.icon,
      CableBusTextures.PartImportSides.icon,
      CableBusTextures.PartMonitorBack.icon,
      this.itemStack.iconIndex,
      CableBusTextures.PartImportSides.icon,
      CableBusTextures.PartImportSides.icon
    )
    rh.setBounds(3f, 3f, 15f, 13f, 13f, 16f)
    rh.renderInventoryBox(renderer)
    rh.setBounds(4f, 4f, 14f, 12f, 12f, 15f)
    rh.renderInventoryBox(renderer)
    rh.setBounds(5f, 5f, 13f, 11f, 11f, 14f)
    rh.renderInventoryBox(renderer)
  }

  @SideOnly(Side.CLIENT)
  override fun renderStatic(x: Int, y: Int, z: Int, rh: IPartRenderHelper, renderer: RenderBlocks?) {
    renderCache = rh.useSimplifiedRendering(x, y, z, this, renderCache)
    rh.setTexture(
      CableBusTextures.PartImportSides.icon,
      CableBusTextures.PartImportSides.icon,
      CableBusTextures.PartMonitorBack.icon,
      this.itemStack.iconIndex,
      CableBusTextures.PartImportSides.icon,
      CableBusTextures.PartImportSides.icon
    )
    rh.setBounds(4f, 4f, 14f, 12f, 12f, 16f)
    rh.renderBlock(x, y, z, renderer)
    rh.setBounds(5f, 5f, 13f, 11f, 11f, 14f)
    rh.renderBlock(x, y, z, renderer)
    rh.setBounds(6f, 6f, 12f, 10f, 10f, 13f)
    rh.renderBlock(x, y, z, renderer)
    rh.setTexture(
      CableBusTextures.PartMonitorSidesStatus.icon,
      CableBusTextures.PartMonitorSidesStatus.icon,
      CableBusTextures.PartMonitorBack.icon,
      this.itemStack.iconIndex,
      CableBusTextures.PartMonitorSidesStatus.icon,
      CableBusTextures.PartMonitorSidesStatus.icon
    )
    rh.setBounds(6f, 6f, 11f, 10f, 10f, 12f)
    rh.renderBlock(x, y, z, renderer)
    renderLights(x, y, z, rh, renderer)
  }


  override fun cableConnectionRenderTo(): Int {
    return 5
  }
  // endregion render

  override fun getTickingRequest(node: IGridNode?): TickingRequest {
    return TickingRequest(TickRates.ExportBus.min, TickRates.ExportBus.max, this.isSleeping, false)
  }

  override fun getRSMode(): RedstoneMode {
    return this.configManager.getSetting(Settings.REDSTONE_CONTROLLED) as RedstoneMode
  }

  override fun tickingRequest(node: IGridNode?, ticksSinceLastCall: Int): TickRateModulation {
    return doBusWork()
  }

  override fun isSleeping(): Boolean {
    return this.fluidHandler == null || super.isSleeping()
  }
}