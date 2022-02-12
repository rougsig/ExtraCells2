package extracells

import extracells.network.GuiHandler

// Just check for cross compilation
// Be clear to delete that file
object MyKotlinCode {
  fun doMagic() = GuiHandler.getGuiId(1)
}
