package extracells.feature.part.core

import appeng.api.networking.security.BaseActionSource
import extracells.core.entity.ECFluidStack

// TODO: move to extracells.core.storage
interface ECFluidMonitor {
  interface Listener {
    fun onFluidsChange(fluids: List<ECFluidStack>)
  }

  /**
   * @return not injected amount
   * Example: [amount] = 1000, in storage 600, returns will be 400
   */
  fun inject(src: BaseActionSource, fluidName: String, amount: Int): Int

  /**
   * @return how much amount can be injected,
   * without injection to storage
   */
  fun simulateInject(src: BaseActionSource, fluidName: String, amount: Int): Int

  /**
   * @return extracted amount
   * Example: [amount] = 1000, in storage 600, returns will be 600
   */
  fun extract(src: BaseActionSource, fluidName: String, amount: Int): Int

  /**
   * @return how much amount can be extracted,
   * without extraction from storage
   */
  fun simulateExtract(src: BaseActionSource, fluidName: String, amount: Int): Int

  fun addListener(listener: Listener)
  fun removeListener(listener: Listener)

  val storedFluids: List<ECFluidStack>
}
