package extracells.api.storage

internal interface IFluidStorage {
  /**
   * @return not injected amount
   * Example: [amount] = 1000, in storage 600, returns will be 400
   */
  fun inject(fluidName: String, amount: Int): Int

  /**
   * @return how much amount can be injected,
   * without injection to storage
   */
  fun simulateInject(fluidName: String, amount: Int): Int

  /**
   * @return extracted amount
   * Example: [amount] = 1000, in storage 600, returns will be 600
   */
  fun extract(fluidName: String, amount: Int): Int

  /**
   * @return how much amount can be extracted,
   * without extraction from storage
   */
  fun simulateExtract(fluidName: String, amount: Int): Int

  // TODO:
  //  maybe create data class for fluid
  val storedFluids: List<Pair<String, Int>>
}
