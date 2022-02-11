package extracells.integration.opencomputers;

import appeng.api.AEApi;
import extracells.item.ItemOCUpgrade;
import li.cil.oc.api.Driver;

public class OpenComputers {
    public static void init(){
        Driver.add(new DriverFluidExportBus());
        Driver.add(new DriverFluidExportBus.Provider());
        Driver.add(new DriverFluidImportBus());
        Driver.add(new DriverFluidImportBus.Provider());
        Driver.add(new DriverOreDictExportBus());
        Driver.add(new DriverOreDictExportBus.Provider());
        Driver.add(new DriverFluidInterface());
        Driver.add(new DriverFluidInterface.Provider());
        Driver.add(new ItemOCUpgrade());
        Driver.add(new ItemOCUpgrade.Provider());
        AEApi.instance().registries().wireless().registerWirelessHandler(new WirelessHandlerUpgradeAE());
        OCRecipes.loadRecipes();
        new ExtraCellsPathProvider();
    }
}
