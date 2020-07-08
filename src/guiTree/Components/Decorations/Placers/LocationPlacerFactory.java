package guiTree.Components.Decorations.Placers;

import java.util.HashMap;
import java.util.Map;

public class LocationPlacerFactory {
    private static Map<String, Placer> placerMap;
    private static boolean initialized = false;

    public static Placer getPlacer(String name) {
        if(!initialized) {
            initialize();
        }
        if(placerMap.containsKey(name)) {
            return placerMap.get(name);
        }
        return null;
    }

    public static void addPlacer(String name, Placer placer) {
        placerMap.put(name, placer);
    }

    private static void initialize() {
        initialized = true;
        placerMap = new HashMap<>();
        placerMap.put("top_left", new TopLeftPlacer());
        placerMap.put("top_right", new TopRightPlacer());
        placerMap.put("top_center", new TopCenterPlacer());
        placerMap.put("middle_left", new MiddleLeftPlacer());
        placerMap.put("middle_right", new MiddleRightPlacer());
        placerMap.put("middle_center", new MiddleCenterPlacer());
        placerMap.put("bottom_left", new BottomLeftPlacer());
        placerMap.put("bottom_right", new BottomRightPlacer());
        placerMap.put("bottom_center", new BottomCenterPlacer());
        placerMap.put("general", new GeneralPlacer());
    }
}
