package extracells.api.storage

import appeng.api.storage.ICellWorkbenchItem

interface IFluidStorageCell : ICellWorkbenchItem {
  val idleDrain: Double
  val bytesPerType: Int
  val totalBytes: Int
  val totalTypes: Int
}
