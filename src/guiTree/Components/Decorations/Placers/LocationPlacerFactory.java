package guiTree.Components.Decorations.Placers;

import java.util.HashMap;
import java.util.Map;

public class LocationPlacerFactory {
    private static Map<String, Class<?>> placerMap;
    private static boolean initialized = false;

    public static Placer getPlacer(String name) {
        if(!initialized) {
            initialize();
        }
        if(placerMap.containsKey(name)) {
            try {
                return (Placer) placerMap.get(name).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void addPlacer(String name, Placer placer) {
        placerMap.put(name, placer.getClass());
    }

    private static void initialize() {
        initialized = true;
        placerMap = new HashMap<>();
        placerMap.put("top_left", TopLeftPlacer.class);
        placerMap.put("top_right", TopRightPlacer.class);
        placerMap.put("top_center", TopCenterPlacer.class);
        placerMap.put("middle_left", MiddleLeftPlacer.class);
        placerMap.put("middle_right", MiddleRightPlacer.class);
        placerMap.put("middle_center", MiddleCenterPlacer.class);
        placerMap.put("bottom_left", BottomLeftPlacer.class);
        placerMap.put("bottom_right", BottomRightPlacer.class);
        placerMap.put("bottom_center", BottomCenterPlacer.class);
        placerMap.put("general", GeneralPlacer.class);
    }
}
