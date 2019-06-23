package com.florinvelesca.beaconapp.pathalgorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class BeaconGraphTest {
    private BeaconGraph beaconGraph = new BeaconGraph();

    @Test
    public void Should_FindPath_When_ThereAreTwoNodes() {
        beaconGraph.addLink(1, 2, 1);

        List<Integer> path = beaconGraph.getPath(1, 2);

        assertThat(path).hasSize(2);
        assertThat(path).isEqualTo(Arrays.asList(1, 2));
    }

    @Test
    public void Should_FindPath_When_ThereIsOnlyOnePath() {
        beaconGraph.addLink(1, 2, 1);
        beaconGraph.addLink(1, 3, 1);
        beaconGraph.addLink(3, 4, 1);
        beaconGraph.addLink(4, 5, 1);

        List<Integer> path = beaconGraph.getPath(1, 5);

        assertThat(path).isEqualTo(Arrays.asList(1, 3, 4, 5));

    }

    @Test
    public void Should_FindShortestPath() {
        beaconGraph.addLink(1, 2, 1);
        beaconGraph.addLink(2, 6, 1);
        beaconGraph.addLink(6, 7, 1);
        beaconGraph.addLink(7, 8, 1);
        beaconGraph.addLink(8, 9, 1);
        beaconGraph.addLink(9, 5, 1);

        beaconGraph.addLink(1, 3, 1);
        beaconGraph.addLink(3, 4, 1);
        beaconGraph.addLink(4, 5, 1);

        List<Integer> path = beaconGraph.getPath(1, 5);

        assertThat(path).isEqualTo(Arrays.asList(1, 3, 4, 5));

    }

    @Test
    public void Should_FindShortestPath_When_SameNodeIsVisitedTwice() {
        beaconGraph.addLink(1, 2, 1);
        beaconGraph.addLink(2, 3, 1);
        beaconGraph.addLink(3, 7, 1);
        beaconGraph.addLink(7, 8, 1);
        beaconGraph.addLink(8, 9, 1);
        beaconGraph.addLink(9, 5, 1);

        beaconGraph.addLink(1, 3, 1);
        beaconGraph.addLink(3, 4, 1);
        beaconGraph.addLink(4, 5, 1);

        List<Integer> path = beaconGraph.getPath(1, 5);

        assertThat(path).isEqualTo(Arrays.asList(1, 3, 4, 5));

    }
}