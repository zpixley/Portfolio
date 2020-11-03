public class Edge implements Comparable<Edge> {
    private int v;
    private int w;
    private String material;
    private int bandwidth;
    private int length;
    private double time;        //  getTime to travel through wire in nanosecs
    public boolean isCopper;

    /**
     * Initializes an edge between nodes v and w of the given material, bandwidth, and length.
     * @param v first vertex
     * @param w second vertex
     * @param material material of wire
     * @param bandwidth bandwidth of wire
     * @param length length of wire
     */
    public Edge(int v, int w, String material, int bandwidth, int length) {
        this.v = v;
        this.w = w;
        this.material = material.toLowerCase();
        this.bandwidth = bandwidth;
        this.length = length;
        if (material.equals("copper")) {
            time = (double) length / 230.0;
            isCopper = true;
        }
        else if (material.equals("optical")) {
            time = (double) length / 200.0;
            isCopper = false;
        }
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public int getLength() {
        return length;
    }

    /**
     * Returns either endpoint of this edge.
     * @return either endpoint of this edge
     */
    public int either() {
        return v;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    /**
     * Returns the getTime of the directed edge.
     * @return the getTime of the directed edge
     */
    public double getTime() {
        return time;
    }

    /**
     * Returns the endpoint of this edge that is different from the given node.
     * @param  node one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the node is not one of the
     *         endpoints of this edge
     */
    public int other(int node) {
        if      (node == v) return w;
        else if (node == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Compares two edges by getTime to travel down wire.
     * @param  that the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     *         the bandwidth of this is less than, equal to, or greater than the
     *         argument edge
     */
    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.time, that.time);
    }

    public String toString() {
        return v + "-" + w + " " + material + " " + bandwidth + " " + length + " " + time;
    }
}