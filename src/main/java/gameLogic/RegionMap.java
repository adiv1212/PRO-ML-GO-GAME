package main.java.gameLogic;

import java.util.*;

import main.java.gameLogic.stone.Point;

public class RegionMap {
    public Collection<Region> regions;
    public Map<Point, Region> regionMap;

    RegionMap(Collection<Region> regions, Map<Point, Region> regionMap) {
        this.regions = regions;
        this.regionMap = regionMap;
    }

    @Override
    public String toString() {
        String result = "";
        for (Region r : regions)
            result += r;
        return result;
    }
}