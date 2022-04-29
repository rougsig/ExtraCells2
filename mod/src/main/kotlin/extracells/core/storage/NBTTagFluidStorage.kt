package extracells.core.storage

import extracells.api.storage.IFluidStorage
import extracells.extension.keys
import net.minecraft.nbt.NBTTagCompound
import kotlin.math.min

//
// ALARM
//
// On change any nbt keys should update migration in file FluidStorageMigrations
//
internal class NBTTagFluidStorage(
  private val storage: NBTTagCompound,
  private val maxTypes: Int,
  private val typeSize: Int,
  private val size: Int,
) : IFluidStorage {
  val nbtTag: NBTTagCompound
    get() = this.storage.copy() as NBTTagCompound

  private val fluidStorage: NBTTagCompound
  var freeSpace = size
    private set

  override val storedFluids: List<Pair<String, Int>>
    get() = this.fluidStorage.keys.map { key -> key to fluidStorage.getInteger(key) }

  init {
    // initializeStoredFluids
    if (!storage.hasKey("fluids")) {
      storage.setTag("fluids", NBTTagCompound())
    }
    this.fluidStorage = migrateFromLegacyToV1(storage).getCompoundTag("fluids")

    // initializeFreeSpace
    this.fluidStorage.keys.forEach { key ->
      this.freeSpace -= this.typeSize
      this.freeSpace -= this.fluidStorage.getInteger(key)
    }
  }

  /**
   * @return not injected amount
   * Example: [amount] = 1000, in [nbtTag] 600, returns will be 400
   */
  override fun inject(fluidName: String, amount: Int): Int {
    if (freeSpace <= 0) return amount
    if (!this.isFluidRegistered(fluidName) && !this.registerFluid(fluidName)) return amount

    val storedFluid = fluidStorage.getInteger(fluidName)
    val maxAmountToInject = min(freeSpace, amount)
    if (maxAmountToInject < 0) error("Inject negative amount of fluid. freeSpace='$freeSpace', amount='$amount'")
    fluidStorage.setInteger(fluidName, storedFluid + maxAmountToInject)
    freeSpace -= maxAmountToInject
    return amount - maxAmountToInject
  }

  /**
   * @return how much amount can be injected,
   * without injection to storage
   */
  override fun simulateInject(fluidName: String, amount: Int): Int {
    // TODO: optimize work without copy
    //  to prevent extra gc overhead
    return this.copy().inject(fluidName, amount)
  }

  /**
   * @return extracted amount
   * Example: [amount] = 1000, in [nbtTag] 600, returns will be 600
   */
  override fun extract(fluidName: String, amount: Int): Int {
    if (!this.isFluidRegistered(fluidName)) return 0

    val storedFluid = fluidStorage.getInteger(fluidName)
    val maxAmountToExtract = min(storedFluid, amount)
    if (maxAmountToExtract < 0) error("Extract negative amount of fluid. freeSpace='$storedFluid', amount='$amount'")
    fluidStorage.setInteger(fluidName, storedFluid - maxAmountToExtract)
    if (fluidStorage.getInteger(fluidName) == 0) unregisterFluid(fluidName)
    freeSpace += maxAmountToExtract
    return maxAmountToExtract
  }

  /**
   * @return how much amount can be extracted,
   * without extraction from storage
   */
  override fun simulateExtract(fluidName: String, amount: Int): Int {
    // TODO: optimize work without copy
    //  to prevent extra gc overhead
    return this.copy().extract(fluidName, amount)
  }

  private fun isFluidRegistered(fluidName: String): Boolean {
    return fluidStorage.hasKey(fluidName)
  }

  /**
   * @return true if fluid successfully registered, false otherwise
   */
  private fun registerFluid(fluidName: String): Boolean {
    check(!this.isFluidRegistered(fluidName)) { "Fluid with name: '$fluidName' already registered" }

    val isFreeTypeExists = fluidStorage.keys.size < this.maxTypes
    val isFreeSpaceExists = freeSpace >= this.maxTypes

    if (!(isFreeTypeExists && isFreeSpaceExists)) return false

    fluidStorage.setInteger(fluidName, 0)
    freeSpace -= typeSize
    return true
  }

  private fun unregisterFluid(fluidName: String) {
    this.fluidStorage.removeTag(fluidName)
    this.freeSpace += this.typeSize
  }

  private fun copy(): NBTTagFluidStorage {
    return NBTTagFluidStorage(
      storage = this.storage.copy() as NBTTagCompound,
      maxTypes = this.maxTypes,
      typeSize = this.typeSize,
      size = this.size,
    )
  }
}
