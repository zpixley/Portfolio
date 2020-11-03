public class Car {
    private String VIN;
    private String make;
    private String model;
    private int price;
    private int mileage;
    private String color;

    public Car() {

    }

    public Car (String VIN, String make, String model, int price, int mileage, String color) {
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.price = price;
        this.mileage = mileage;
        this.color = color;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make.toUpperCase();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model.toUpperCase();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color.toUpperCase();
    }

    public String toString() {
        return "Vin: " + VIN +
                "\nMake: " + make +
                "\nModel: " + model +
                "\nPrice: $" + price +
                "\nMileage: " + mileage +
                "\nColor: " + color;
    }
}