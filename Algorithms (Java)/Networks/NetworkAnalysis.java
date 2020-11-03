import java.util.*;


public class NetworkAnalysis {
    private static final String mainMenu = "\n---------------------------------------------------------------------------" +
            "\n\nChoose an option:" +
            "\n1. Find lowest latency path" +
            "\n2. Check if network can be copper-only connected" +
            "\n3. Find max data transfer between nodes" +
            "\n4. Find minimum average latency spanning tree" +
            "\n5. Determine if the network could survive two node failures" +
            "\n6. Quit" +
            "\n--> ";

    public static void main(String[] args) {
        Scanner inScan = new Scanner(System.in);
        String filename;
        Network network;
        int choice = 0, firstNode, secondNode;


        if (args.length < 1) {
            System.out.println("Please include filename");
            return;
        }

        filename =  args[0];
        network = new Network(filename);

        System.out.print(mainMenu);
        choice = validateChoice(inScan.nextLine(), inScan);

        while (choice != 6) {
            switch (choice) {
                //  Lowest latency path
                case 1:
                    System.out.print("Source Node: ");
                    firstNode = Integer.parseInt(inScan.nextLine());
                    System.out.print("Destination Node: ");
                    secondNode = Integer.parseInt(inScan.nextLine());
                    network.findLowestLatencyPath(firstNode, secondNode);
                    break;
                //  Copper-only connected
                case 2:
                    network.isCopperOnlyConnected();
                    break;
                //  Max data transfer
                case 3:
                    System.out.print("Source Node: ");
                    firstNode = Integer.parseInt(inScan.nextLine());
                    System.out.print("Destination Node: ");
                    secondNode = Integer.parseInt(inScan.nextLine());
                    network.findMaxBandwidth(firstNode, secondNode);
                    break;
                //  Minimum average latency spanning tree
                case 4:
                    network.findMinimumSpanningTree();
                    break;
                //  Check vertex failures
                case 5:
                    network.canSurviveFailures();
                    break;
            }


            System.out.print(mainMenu);
            choice = validateChoice(inScan.nextLine(), inScan);
        }

    }

    private static int validateChoice(String s, Scanner inScan) {
        int choice = Integer.parseInt(s);

        while (!(choice >= 1 && choice <= 6)) {
            System.out.println("Invalid choice, try again");
            System.out.print(mainMenu);
            choice = Integer.parseInt(inScan.nextLine());
        }

        return choice;
    }
}
