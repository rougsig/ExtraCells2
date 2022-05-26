package extracells

import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import extracells.ExtraCells.MOD_ID
import extracells.core.proxy.CommonProxy
import extracells.feature.item.ECItem
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

@Mod(
  modid = MOD_ID,
  name = "Extra Cells",
  version = "GRADLETOKEN_VERSION",
  modLanguage = "kotlin",
  dependencies = "required-after:forgelin;after:LogisticsPipes|Main;after:Waila;required-after:appliedenergistics2",
  modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
)
object ExtraCells {
  const val MOD_ID = "extracells"

  @JvmStatic
  @SidedProxy(
    clientSide = "extracells.core.proxy.ClientProxy",
    serverSide = "extracells.core.proxy.ServerProxy",
  )
  lateinit var proxy: CommonProxy

  @JvmStatic
  @Mod.InstanceFactory
  fun instance() = ExtraCells

  @JvmStatic
  var VERSION = ""
    private set

  val creativeTab = object : CreativeTabs("ExtraCells") {
    override fun getIconItemStack(): ItemStack {
      return ItemStack(ECItem.FluidCell.item)
    }

    override fun getTabIconItem(): Item {
      return ECItem.FluidCell.item
    }
  }

  @EventHandler
  fun preInit(event: FMLPreInitializationEvent) {
    VERSION = Loader.instance().activeModContainer().version

    proxy.registerTextures()
    proxy.registerGuiHandler()
    proxy.registerItems()
    proxy.registerBlocks()
  }

  @EventHandler
  fun init(event: FMLInitializationEvent) {
    proxy.registerRenderers()
    proxy.registerTileEntities()
    proxy.registerAppengIntegration()
    proxy.registerDebugTools()
  }

  @EventHandler
  fun postInit(event: FMLPostInitializationEvent) {
    proxy.registerNetworkHandler()
  }
}
