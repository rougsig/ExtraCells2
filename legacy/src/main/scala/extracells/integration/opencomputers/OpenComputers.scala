package extracells.integration.opencomputers

import appeng.api.AEApi
import extracells.integration.Integration
import extracells.item.ItemOCUpgrade
import li.cil.oc.api.Driver
object OpenComputers {
	
	def init(){
		Driver.add(new DriverFluidExportBus)
		Driver.add(new DriverFluidExportBus.Provider)
		Driver.add(new DriverFluidImportBus)
		Driver.add(new DriverFluidImportBus.Provider)
		Driver.add(new DriverOreDictExportBus)
		Driver.add(new DriverOreDictExportBus.Provider)
		Driver.add(new DriverFluidInterface)
		Driver.add(new DriverFluidInterface.Provider)
		Driver.add(ItemOCUpgrade.getInstance())
		Driver.add(ItemOCUpgrade.getInstance().Provider)
		AEApi.instance.registries.wireless.registerWirelessHandler(WirelessHandlerUpgradeAE)
		OCRecipes.loadRecipes()
		ExtraCellsPathProvider
	}

}
