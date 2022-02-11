package extracells.util;

import buildcraft.api.fuels.BuildcraftFuelRegistry;
import buildcraft.api.fuels.IFuel;
import cpw.mods.fml.common.Optional;
import extracells.integration.Integration;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;
import java.util.Map;

public class FuelBurnTime {

    private static final Map<Fluid, Integer> fluidBurnTimes = new HashMap<>();

    public static void registerFuel(Fluid fluid, int burnTime) {
        if (!fluidBurnTimes.containsKey(fluid)) {
            fluidBurnTimes.put(fluid, burnTime);
        }
    }

    public static int getBurnTime(Fluid fluid) {
        if (fluid == null) return 0;
        if (fluidBurnTimes.containsKey(fluid)) {
            return fluidBurnTimes.get(fluid);
        }
        if (Integration.Mods.BCFUEL.isEnabled()) {
            return getBCBurnTime(fluid);
        }
        return 0;
    }

    @Optional.Method(modid = "BuildCraftAPI|fuels")
    private static int getBCBurnTime(Fluid fluid) {
        for (IFuel fuel : BuildcraftFuelRegistry.fuel.getFuels()) {
            if (fuel.getFluid() == fluid) {
                return fuel.getTotalBurningTime();
            }
        }
        return 0;
    }
}