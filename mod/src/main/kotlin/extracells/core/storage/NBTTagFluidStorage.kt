package extracells.core.storage

import extracells.extension.keys
import net.minecraft.nbt.NBTTagCompound
import kotlin.math.min

internal class NBTTagFluidStorage(
  private val storage: NBTTagCompound,
  private val maxTypes: Int,
  private val typeSize: Int,
  private val size: Int,
) : IFluidStorage {
  val nbtTag: NBTTagCompound
    get() = this.storage.copy() as NBTTagCompound

  private val storedFluids: NBTTagCompound
  var freeSpace = size
    private set

  init {
    // initializeStoredFluids
    if (!storage.hasKey("fluids")) {
      storage.setTag("fluids", NBTTagCompound())
    }
    this.storedFluids = storage.getCompoundTag("fluids")

    // initializeFreeSpace
    this.storedFluids.keys.forEach { key ->
      this.freeSpace -= this.typeSize
      this.freeSpace -= this.storedFluids.getInteger(key)
    }
  }

  /**
   * @return not injected amount
   * Example: [amount] = 1000, in [nbtTag] 600, returns will be 400
   */
  override fun inject(fluidName: String, amount: Int): Int {
    if (freeSpace <= 0) return amount
    if (!this.isFluidRegistered(fluidName) && !this.registerFluid(fluidName)) return amount

    val storedFluid = storedFluids.getInteger(fluidName)
    val maxAmountToInject = min(freeSpace, amount)
    if (maxAmountToInject < 0) error("Inject negative amount of fluid. freeSpace='$freeSpace', amount='$amount'")
    storedFluids.setInteger(fluidName, storedFluid + maxAmountToInject)
    freeSpace -= maxAmountToInject
    return amount - maxAmountToInject
  }

  /**
   * @return how much amount can be injected,
   * without injection to storage
   */
  override fun simulateInject(fluidName: String, amount: Int): Int {
    return this.copy().inject(fluidName, amount)
  }

  /**
   * @return extracted amount
   * Example: [amount] = 1000, in [nbtTag] 600, returns will be 600
   */
  override fun extract(fluidName: String, amount: Int): Int {
    if (!this.isFluidRegistered(fluidName)) return amount

    val storedFluid = storedFluids.getInteger(fluidName)
    val maxAmountToExtract = min(storedFluid, amount)
    if (maxAmountToExtract < 0) error("Extract negative amount of fluid. freeSpace='$storedFluid', amount='$amount'")
    storedFluids.setInteger(fluidName, storedFluid - maxAmountToExtract)
    if (storedFluids.getInteger(fluidName) == 0) unregisterFluid(fluidName)
    freeSpace += maxAmountToExtract
    return maxAmountToExtract
  }

  /**
   * @return how much amount can be extracted,
   * without extraction from storage
   */
  override fun simulateExtract(fluidName: String, amount: Int): Int {
    return this.copy().extract(fluidName, amount)
  }

  private fun isFluidRegistered(fluidName: String): Boolean {
    return storedFluids.hasKey(fluidName)
  }

  /**
   * @return true if fluid successfully registered, false otherwise
   */
  private fun registerFluid(fluidName: String): Boolean {
    check(!this.isFluidRegistered(fluidName)) { "Fluid with name: '$fluidName' already registered" }

    val isFreeTypeExists = storedFluids.keys.size < this.maxTypes
    val isFreeSpaceExists = freeSpace >= this.maxTypes

    if (!(isFreeTypeExists && isFreeSpaceExists)) return false

    storedFluids.setInteger(fluidName, 0)
    freeSpace -= typeSize
    return true
  }

  private fun unregisterFluid(fluidName: String) {
    this.storedFluids.removeTag(fluidName)
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
