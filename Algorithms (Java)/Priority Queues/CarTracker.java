import java.io.*;
import java.util.*;

public class CarTracker {
    public static void main(String[] args) {
        File inFile = new File("cars.txt");
        Scanner inScan = new Scanner(System.in);
        CarPQManager pq = new CarPQManager(32);
        String mainMenu = "\nPick an option:\n" +
                "1. Add a car\n" +
                "2. Update a car\n" +
                "3. Remove a car\n" +
                "4. Retrieve the lowest price car\n" +
                "5. Retrieve the lowest mileage car\n" +
                "6. Retrieve the lowest price car by make and model\n" +
                "7. Retrieve the lowest mileage car by make and model\n" +
                "8. Quit\n" +
                "-> ";
        try {
            Scanner fScan = new Scanner(inFile);
            fScan.nextLine();
            while (fScan.hasNextLine()) {
                String l = fScan.nextLine();
                String[] parts = l.split(":");
                Car c = new Car(parts[0], parts[1], parts[2], new Integer(parts[3]), new Integer(parts[4]), parts[5]);
                pq.addCar(c);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File 'cars.txt' cannot be found.");
            return;
        }

        while (true) {
            Car c;
            String v;
            System.out.print(mainMenu);
            int choice = inScan.nextInt();
            inScan.nextLine();
            switch (choice) {
                //  Add car
                case 1:
                    c = new Car();
                    System.out.print("VIN: ");
                    c.setVIN(inScan.nextLine());
                    System.out.print("Make: ");
                    c.setMake(inScan.nextLine());
                    System.out.print("Model: ");
                    c.setModel(inScan.nextLine());
                    System.out.print("Price: ");
                    c.setPrice(inScan.nextInt());
                    inScan.nextLine();
                    System.out.print("Mileage: ");
                    c.setMileage(inScan.nextInt());
                    inScan.nextLine();
                    System.out.print("Color: ");
                    c.setColor(inScan.nextLine());
                    if (!pq.contains(c.getVIN())) {
                        if (pq.addCar(c)) {
                            System.out.println("\nCar has been added to tracker");
                        }
                    }
                    else {
                        System.out.println("\nCar is already in tracker");
                    }
                    break;
                //  Update car
                case 2:
                    System.out.println("\nType the VIN of the car you'd like to update and press 'Enter':");
                    v = inScan.nextLine();
                    if (pq.contains(v)) {
                        System.out.print("\nWhat would you like to update?\n" +
                                "1. Price\n" +
                                "2. Mileage\n" +
                                "3. Color\n" +
                                "->");
                        int o = inScan.nextInt();
                        inScan.nextLine();
                        switch (o) {
                            case 1:
                                System.out.print("New price: ");
                                int newPrice = inScan.nextInt();
                                inScan.nextLine();
                                pq.updatePrice(v, newPrice);
                                break;
                            case 2:
                                System.out.print("New mileage: ");
                                int newMileage = inScan.nextInt();
                                inScan.nextLine();
                                pq.updateMileage(v, newMileage);
                                break;
                            case 3:
                                System.out.print("New color: ");
                                String newColor = inScan.nextLine();
                                pq.updateColor(v, newColor);
                                break;
                        }

                    }
                    break;
                //  Remove car
                case 3:
                    System.out.println("\nType the VIN of the car you'd like to remove and press 'Enter': ");
                    v = inScan.nextLine();
                    if (pq.contains(v)) {
                        if (pq.removeCar(v)) {
                            System.out.println("\nCar has been removed from tracker");
                        }
                    }
                    else {
                        System.out.println("\nNo car in tracker with VIN: " + v);
                    }
                    break;
                // Retrieve lowest price
                case 4:
                    c = pq.getMinPrice();
                    System.out.println("\nHere are the details of the car with the lowest overall price:\n" + c);
                    break;
                //  Retrieve lowest mileage
                case 5:
                    c = pq.getMinMileage();
                    System.out.println("\nHere are the details of the car with the lowest overall mileage:\n" + c);
                    break;
                //  Retrieve lowest price by make and model
                case 6:
                    System.out.print("Make: ");
                    String make = inScan.nextLine();
                    System.out.print("Model: ");
                    String model = inScan.nextLine();
                    c = pq.getMinPrice(make + model);
                    System.out.println("\nHere are the details of the lowest priced " + make + " " + model + ":\n" + c);
                    break;
                //  Retrieve lowest mileage by make and model
                case 7:
                    System.out.print("Make: ");
                    make = inScan.nextLine();
                    System.out.print("Model: ");
                    model = inScan.nextLine();
                    c = pq.getMinMileage(make + model);
                    System.out.println("\nHere are the details of the least driven " + make + " " + model + ":\n" + c);
                    break;
                //  Quit
                case 8:
                    return;
                default:
                    System.out.println("\nInvalid choice.\n");
            }
        }
    }
}
