import java.io.*;
import java.util.*;

public class Network {
    private int numNodes;
    private LinkedList<Edge> adj[];
    private boolean copperOnlyConnected;
    private boolean surviveFailures;

    /**
     * Constructs network from data file structured like network_data1.txt
     * @param filename name of source file
     */
    public Network(String filename) {
        File inFile = new File(filename);
        Scanner fScan;

        try {
            fScan = new Scanner(inFile);
        }
        catch (FileNotFoundException e) {
            System.out.println("File '" + filename + "' not found in source folder");
            return;
        }

        numNodes = Integer.parseInt(fScan.nextLine());
        adj = (LinkedList<Edge>[]) new LinkedList[numNodes];
        for (int i = 0; i < numNodes; i++) {
            adj[i] = new LinkedList<Edge>();
        }

        while (fScan.hasNext()) {
            String s = fScan.nextLine();
            String[] parts = s.split(" ");

            int v = Integer.parseInt(parts[0]);
            int w = Integer.parseInt(parts[1]);

            Edge edge = new Edge(v, w, parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
            Edge reverse = new Edge(w, v, parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));

            adj[v].add(edge);
            adj[w].add(reverse);
        }

        copperOnlyConnected = true;
        CopperCC copperChecker = new CopperCC(this);
        if (copperChecker.count() > 1) {
            copperOnlyConnected = false;
        }

        surviveFailures = true;
        for (int i = 0; i < numNodes - 1; i++) {
            for (int j = i + 1; j < numNodes; j++) {
                FailureCC failureChecker = new FailureCC(this, i, j);
                if (failureChecker.count() > 1) {
                    surviveFailures = false;
                    break;
                }
            }

            if (surviveFailures == false) {
                break;
            }
        }
    }

    public void findLowestLatencyPath(int v, int w) {
        int minBandwidth;
        LatencyDijkstra lowestLatency;
        lowestLatency = new LatencyDijkstra(this, v);
        Iterable<Edge> lowestLatencyPath = lowestLatency.pathTo(w);
        minBandwidth = lowestLatencyPath.iterator().next().getBandwidth();
        StringBuilder path = new StringBuilder("Path: ");
        for (Edge e : lowestLatencyPath) {
            if (e.getBandwidth() < minBandwidth) {
                minBandwidth = e.getBandwidth();
            }
            path.append(e.from() + "-");
        }
        path.append(w);
        System.out.println("\n" + path);
        System.out.println("Available Bandwidth: " + minBandwidth);
    }

    public boolean isCopperOnlyConnected() {
        if (copperOnlyConnected) {
            System.out.println("\nThis network can be connected by only copper wires");
        }
        else {
            System.out.println("\nThis network cannot be connected by only copper wires");
        }
        return copperOnlyConnected;
    }

    public void findMaxBandwidth(int v, int w) {
        int max;
        BandwidthDijkstra maxBandwidth;
        maxBandwidth = new BandwidthDijkstra(this, v);
        Iterable<Edge> lowestBandwidthPath = maxBandwidth.pathTo(w);
        max = lowestBandwidthPath.iterator().next().getBandwidth();
        for (Edge e : lowestBandwidthPath) {
            if (e.getBandwidth() < max) {
                max = e.getBandwidth();
            }
        }
        System.out.println("\nMax Bandwidth (" + v + "-" + w + "): " + max);
    }

    public void findMinimumSpanningTree() {
        KruskalMST mst = new KruskalMST(this);
        System.out.println("\nEdges in tree:");
        for (Edge e : mst.edges()) {
            System.out.println(e);
        }
        System.out.printf("Tree Weight: %.5f\n", mst.weight());
    }

    public boolean canSurviveFailures() {
        if (surviveFailures) {
            System.out.println("\nThis network can survive the failures of any two nodes");
        }
        else {
            System.out.println("\nThis network could not survive the failures of any two arbitrary nodes");
        }
        return surviveFailures;
    }

    public int getNumNodes() {
        return numNodes;
    }

    /**
     * Returns the edges incident on node v
     * @param  v the node
     * @return the edges incident on vertex v as an ArrayList
     */
    public LinkedList<Edge> adj(int v) {
        return adj[v];
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public LinkedList<Edge> edges() {
        LinkedList<Edge> list = new LinkedList<Edge>();
        for (int v = 0; v < numNodes; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(numNodes + "\n");
        for (int v = 0; v < numNodes; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

}
