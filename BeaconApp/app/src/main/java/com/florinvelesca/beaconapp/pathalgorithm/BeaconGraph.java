package com.florinvelesca.beaconapp.pathalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BeaconGraph {
    private static final int NO_LINK = -1;

    private Map<Integer, List<Link>> links = new HashMap<>();
    private List<List<Integer>> paths = new ArrayList<>();

    public void addLink(int beaconId1, int beaconId2, int distance) {
        List<Link> beacon1Links = getBeaconLinks(beaconId1);
        List<Link> beacon2Links = getBeaconLinks(beaconId2);

        beacon1Links.add(new Link(beaconId2, distance));
        beacon2Links.add(new Link(beaconId1, distance));
    }

    private List<Link> getBeaconLinks(int beaconId) {
        List<Link> nodeLinks;
        if (!links.containsKey(beaconId)) {
            nodeLinks = new ArrayList<>(4);
            links.put(beaconId, nodeLinks);
        } else {
            nodeLinks = links.get(beaconId);
        }
        return nodeLinks;
    }

    private boolean hasDirectLink(int beacon1, int beacon2) {
        List<Link> beacon1Links = links.get(beacon1);
        for (Link beacon1Link : beacon1Links) {
            if (beacon1Link.getBeacon() == beacon2) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> getAllNotVisitedBeacons(int beacon1, Set<Integer> visitedBeacons) {
        List<Integer> notVisitedBeacons = new ArrayList<>();

        List<Link> beacon1Links = links.get(beacon1);
        for (Link link : beacon1Links) {
            if (!visitedBeacons.contains(link.getBeacon())) {
                notVisitedBeacons.add(link.getBeacon());
            }
        }

        return notVisitedBeacons;
    }

    public List<Integer> getPath(int beacon1, int beacon2) {
        paths.clear();

        buildPath(new ArrayList<Integer>(), new HashSet<Integer>(), beacon1, beacon2);

        List<Integer> shortestPath = Collections.emptyList();
        int shortestPathLength = Integer.MAX_VALUE;

        for (List<Integer> path : paths) {
            if (path.size() < shortestPathLength) {
                shortestPathLength = path.size();
                shortestPath = path;
            }
        }
        return shortestPath;
    }

    private void buildPath(List<Integer> path, Set<Integer> visitedBeacons, int nextBeacon, int finalBeacon) {

        visitedBeacons.add(nextBeacon);
        path.add(nextBeacon);

        if (hasDirectLink(nextBeacon, finalBeacon)) {
            path.add(finalBeacon);
            paths.add(new ArrayList<>(path));
            path.remove(path.size() - 1);

        } else {

            List<Integer> notVisitedBeacons = getAllNotVisitedBeacons(nextBeacon, visitedBeacons);

            for (Integer notVisitedBeacon : notVisitedBeacons) {
                buildPath(path, visitedBeacons, notVisitedBeacon, finalBeacon);
            }
        }

        visitedBeacons.remove(nextBeacon);
        path.remove(path.size() - 1);

    }
}
