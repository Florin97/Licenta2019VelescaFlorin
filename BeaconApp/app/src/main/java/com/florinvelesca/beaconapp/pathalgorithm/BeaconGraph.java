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


    private int selectFirstNotSelectedBeacon(int beacon1, Set<Integer> visitedBeacons) {
        List<Link> beacon1Links = links.get(beacon1);
        for (Link link : beacon1Links) {
            if (!visitedBeacons.contains(link.getBeacon())) {
                return link.getBeacon();
            }
        }

        return NO_LINK;
    }

    public List<Integer> getPath(int beacon1, int beacon2) {
        int currentBeacon = beacon1;
        List<Integer> path = new ArrayList<>();
        Set<Integer> visitedBeacons = new HashSet<>();

        while (currentBeacon != NO_LINK) {
            visitedBeacons.add(currentBeacon);
            path.add(currentBeacon);
            if (hasDirectLink(currentBeacon, beacon2)) {
                path.add(beacon2);
                return path;
            }

            currentBeacon = selectFirstNotSelectedBeacon(currentBeacon, visitedBeacons);
        }

        return Collections.emptyList();
    }
}
