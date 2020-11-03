public class CarPQManager {
    private PricePQ pricePQ;
    private MileagePQ mileagePQ;

    public CarPQManager(int size) {
        pricePQ = new PricePQ(size);
        mileagePQ = new MileagePQ(size);
    }

    public boolean addCar(Car c) {
        pricePQ.insert(c);
        mileagePQ.insert(c);
        return true;
    }

    public boolean contains(String v) {
        return pricePQ.contains(v);
    }

    public boolean updatePrice(String v, int newPrice) {
        pricePQ.updatePrice(v, newPrice);
        return true;
    }

    public boolean updateMileage(String v, int newMileage) {
        mileagePQ.updateMileage(v, newMileage);
        return true;
    }

    public boolean updateColor(String v, String newColor) {
        pricePQ.updateColor(v, newColor);
        return true;
    }

    public boolean removeCar(String v) {
        pricePQ.delete(v);
        mileagePQ.delete(v);
        return true;
    }

    public Car getMinPrice() {
        return pricePQ.getMin();
    }

    public Car getMinPrice(String makeModel) {
        return pricePQ.getMin(makeModel);
    }

    public Car getMinMileage() {
        return mileagePQ.getMin();
    }

    public Car getMinMileage(String makeModel) {
        return mileagePQ.getMin(makeModel);
    }
}