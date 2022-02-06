package extracells.part

import extracells.inventory.HandlerPartStorageGas


class PartGasStorageScala extends PartFluidStorage{
  handler = new HandlerPartStorageGas(this)
}
