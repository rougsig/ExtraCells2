package extracells

import appeng.api.AEApi
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import extracells.client.ECTextureRegister
import extracells.core.storage.FluidCellHandler
import extracells.debug.ShowNBTCommand
import extracells.feature.ECBlock
import extracells.feature.certustank.CertusTankRenderHandler
import extracells.feature.certustank.CertusTankTileEntity
import extracells.integration.Integration
import extracells.item.EC2Item
import extracells.network.ChannelHandler
import extracells.network.GuiHandler
import extracells.proxy.CommonProxy
import extracells.util.ExtraCellsEventHandler
import extracells.util.NameHandler
import extracells.wireless.AEWirelessTermHandler
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import java.io.File
import extracells.util.FluidCellHandler as LegacyFluidCellHandler

@Mod(
  modid = "extracells",
  name = "Extra Cells",
  version = "GRADLETOKEN_VERSION",
  modLanguage = "kotlin",
  dependencies = "required-after:forgelin;after:LogisticsPipes|Main;after:Waila;required-after:appliedenergistics2",
  modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
)
object ExtraCells {
  private const val IS_LEGACY_FLUID_CELL_HANDLER_ENABLED = false
  private const val IS_LEGACY_BLOCKS_ENABLED = false

  @JvmStatic
  @SidedProxy(
    clientSide = "extracells.proxy.ClientProxy",
    serverSide = "extracells.proxy.CommonProxy",
  )
  lateinit var proxy: CommonProxy

  init {
    ExtracellsLegacy.instance = this
  }

  @JvmStatic
  @Mod.InstanceFactory
  fun instance() = ExtraCells

  @JvmStatic
  var VERSION = ""

  lateinit var configFolder: File
  var shortenedBuckets = ExtracellsLegacy.shortenedBuckets
  var dynamicTypes = ExtracellsLegacy.dynamicTypes
  val integration = Integration()

  val creativeTab = object : CreativeTabs("ExtraCells") {
    override fun getIconItemStack(): ItemStack {
      return ItemStack(EC2Item.FluidCell.item)
    }

    override fun getTabIconItem(): Item {
      return EC2Item.FluidCell.item
    }
  }

  @EventHandler
  fun init(event: FMLInitializationEvent) {
    AEApi.instance().registries().recipes().addNewSubItemResolver(NameHandler())
    AEApi.instance().registries().wireless().registerWirelessHandler(AEWirelessTermHandler())
    if (IS_LEGACY_FLUID_CELL_HANDLER_ENABLED)
      AEApi.instance().registries().cell().addCellHandler(LegacyFluidCellHandler())
    val handler = ExtraCellsEventHandler()
    FMLCommonHandler.instance().bus().register(handler)
    MinecraftForge.EVENT_BUS.register(handler)
    proxy.registerMovables()
    proxy.registerRenderers()
    proxy.registerTileEntities()
    proxy.registerFluidBurnTimes()
    proxy.addRecipes(configFolder)
    ChannelHandler.registerMessages()
    integration.init()

    // New
    ClientCommandHandler.instance.registerCommand(ShowNBTCommand())
    GameRegistry.registerTileEntity(CertusTankTileEntity::class.java, "tileEntityCertusTank")
    CertusTankRenderHandler.register()
    if (!IS_LEGACY_FLUID_CELL_HANDLER_ENABLED)
      AEApi.instance().registries().cell().addCellHandler(FluidCellHandler())
  }

  @EventHandler
  fun postInit(event: FMLPostInitializationEvent) {
    integration.postInit()
  }

  @EventHandler
  fun preInit(event: FMLPreInitializationEvent) {
    ExtracellsLegacy.proxy = this.proxy
    VERSION = Loader.instance().activeModContainer().version
    configFolder = event.getModConfigurationDirectory()

    NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler())

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
    integration.preInit()

    // New
    // TODO:
    //  Move to CommonProxy
    MinecraftForge.EVENT_BUS.register(ECTextureRegister())

    EC2Item.values()
      .forEach { GameRegistry.registerItem(it.item, it.itemName) }

    if (IS_LEGACY_BLOCKS_ENABLED) {
      proxy.registerBlocks()
    } else {
      ECBlock.values()
        .forEach { GameRegistry.registerBlock(it.block, it.itemClass, it.internalName) }
    }
  }
}
