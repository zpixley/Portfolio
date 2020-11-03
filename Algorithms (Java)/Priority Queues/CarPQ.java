import java.util.HashMap;

public abstract class CarPQ {
    public int n;
    protected Car[] pq;
    protected HashMap<String, Integer> map;
    protected HashMap<String, Car> makeModelMap;

    CarPQ() {

    }

    public abstract void insert(Car c);

    public void delete(String v) {
        int index = map.get(v);
        Car c = pq[index];
        exchange(index, --n);
        swim(index);
        sink(index);
        map.remove(v);
        if (makeModelMap.get(c.getMake() + c.getModel()) == c) {
            replaceMinMakeModel(pq[n].getMake() + pq[n].getModel());
        }
        pq[n] = null;
    }
	
	public void updatePrice(String v, int newPrice) {
        Car c = fetchCarByVIN(v);
        c.setPrice(newPrice);
        swim(map.get(c.getVIN()));
        sink(map.get(c.getVIN()));
        if (c.getPrice() < makeModelMap.get(c.getMake() + c.getModel()).getPrice()) {
            makeModelMap.replace(c.getMake() + c.getModel(), c);
        }
    }

    public void updateMileage(String v, int newMileage) {
        Car c = fetchCarByVIN(v);
        c.setMileage(newMileage);
        swim(map.get(c.getVIN()));
        sink(map.get(c.getVIN()));
        if (c.getMileage() < makeModelMap.get(c.getMake() + c.getModel()).getMileage()) {
            makeModelMap.replace(c.getMake() + c.getModel(), c);
        }
    }

    public void updateColor(String v, String newColor) {
        Car c = fetchCarByVIN(v);
        c.setColor(newColor);
    }

    public Car getMin() {
        return pq[0];
    }

    public Car getMin(String makeModel) {
        return makeModelMap.get(makeModel);
    }

    public boolean contains(String v) {
        if (map.get(v) == null) {
            return false;
        }
        return true;
    }

    public Car fetchCarByVIN(String v) {
        int index = map.get(v);
        return pq[index];
    }

    protected void swim(int k) {
        while (k > 0 && lesser(k, (k-1)/2)) {
            exchange(k, (k - 1) / 2);
            k = (k - 1) / 2;
        }
    }

    protected void sink(int k) {
        while ((2 * k + 1) < n) {
            int j = 2 * k + 1;

            if (j < n && lesser(j + 1, j)) {
                j++;
            }
            else {
                break;
            }
            exchange(k, j);
            k = j;
        }
    }

    protected void exchange(int i, int j) {
        map.remove(pq[i].getVIN());
        map.remove(pq[j].getVIN());

        Car temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        map.put(pq[i].getVIN(), i);
        map.put(pq[j].getVIN(), j);
    }

    abstract boolean lesser(int i, int j);

    abstract void replaceMinMakeModel(String makeModel);
}