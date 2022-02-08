package extracells

import appeng.api.AEApi
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import extracells.integration.Integration
import extracells.ExtracellsLegacy
import extracells.network.ChannelHandler
import extracells.network.GuiHandler
import extracells.proxy.CommonProxy
import extracells.render.RenderHandler
import extracells.util.ExtraCellsEventHandler
import extracells.util.FluidCellHandler
import extracells.util.NameHandler
import extracells.wireless.AEWirelessTermHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import java.io.File

@Mod(
  modid = "extracells",
  name = "Extra Cells",
  version = "GRADLETOKEN_VERSION",
  modLanguage = "kotlin",
  dependencies = "required-after:forgelin;after:LogisticsPipes|Main;after:Waila;required-after:appliedenergistics2",
  modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
)
object Extracells {
  @JvmStatic
  @SidedProxy(
    clientSide = "extracells.proxy.ClientProxy",
    serverSide = "extracells.proxy.CommonProxy",
  )
  lateinit var proxy: CommonProxy

  @JvmStatic
  @Mod.InstanceFactory
  fun instance() = Extracells

  @JvmStatic
  var VERSION = ""

  lateinit var configFolder: File
  var shortenedBuckets = ExtracellsLegacy.THIS().shortenedBuckets()
  var dynamicTypes = ExtracellsLegacy.THIS().dynamicTypes()
  val integration = Integration()

  @EventHandler
  fun init(event: FMLInitializationEvent) {
    AEApi.instance().registries().recipes().addNewSubItemResolver(NameHandler())
    AEApi.instance().registries().wireless().registerWirelessHandler(AEWirelessTermHandler())
    AEApi.instance().registries().cell().addCellHandler(FluidCellHandler())
    val handler = ExtraCellsEventHandler()
    FMLCommonHandler.instance().bus().register(handler)
    MinecraftForge.EVENT_BUS.register(handler)
    proxy.registerMovables()
    proxy.registerRenderers()
    proxy.registerTileEntities()
    proxy.registerFluidBurnTimes()
    proxy.addRecipes(configFolder)
    ChannelHandler.registerMessages()
    RenderingRegistry.registerBlockHandler(RenderHandler(RenderingRegistry.getNextAvailableRenderId()))
    integration.init()
  }

  @EventHandler
  fun postInit(event: FMLPostInitializationEvent) {
    integration.postInit()
  }

  @EventHandler
  fun preInit(event: FMLPreInitializationEvent) {
    ExtracellsLegacy.THIS().setProxy(this.proxy)
    VERSION = Loader.instance().activeModContainer().version
    configFolder = event.getModConfigurationDirectory()

    NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler.THIS())

    // Config
    val config =
      Configuration(File(configFolder.path + File.separator + "AppliedEnergistics2" + File.separator + "extracells.cfg"))
    config.load()
    shortenedBuckets =
      config.get("Tooltips", "shortenedBuckets", true, "Shall the guis shorten large mB values?").getBoolean(true)
    dynamicTypes = config.get(
      "Storage Cells",
      "dynamicTypes",
      true,
      "Should the mount of bytes needed for a  type depend on the cellsize?"
    ).getBoolean(true)
    integration.loadConfig(config)

    config.save()

    proxy.registerItems()
    proxy.registerBlocks()
    integration.preInit()
  }
}
