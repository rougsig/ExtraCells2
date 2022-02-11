package extracells.integration.opencomputers;

import li.cil.oc.api.API;
import li.cil.oc.api.detail.ItemInfo;
import li.cil.oc.common.item.data.DroneData;
import li.cil.oc.common.item.data.RobotData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class OCUtils {

    public static boolean isRobot(ItemStack stack) {
        ItemInfo item = API.items.get(stack);
        if (item == null) return false;
        return Objects.equals(item.name(), "robot");
    }

    public static boolean isDrone(ItemStack stack) {
        ItemInfo item = API.items.get(stack);
        if (item == null) return false;
        return Objects.equals(item.name(), "drone");
    }

    public static ItemStack getComponent(RobotData robot, Item item, int meta) {
        for(ItemStack component : robot.components()){
            if(component != null && component.getItem() == item) {
                return component;
            }
        }
        return null;
    }

    public static ItemStack getComponent(RobotData robot, Item item) {
        return getComponent(robot, item, 0);
    }

    public static ItemStack getComponent(DroneData drone, Item item, int meta) {
        for(ItemStack component : drone.components()){
            if(component != null && component.getItem() == item) {
                return component;
            }
        }
        return null;
    }

    public static ItemStack getComponent(DroneData drone, Item item) {
        return getComponent(drone, item, 0);
    }
}