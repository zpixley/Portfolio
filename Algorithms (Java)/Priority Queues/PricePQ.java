import java.util.HashMap;

public class PricePQ extends CarPQ {
    PricePQ(int size) {
        n = 0;
        pq = new Car[size];
        map = new HashMap<>(size);
        makeModelMap = new HashMap<>(size);
    }

    public void insert(Car c) {
        pq[n] = c;
        map.put(c.getVIN(), n);

        swim(n);
        n++;

        //  increase size if full
        if (n == pq.length - 1) {
            int newSize = pq.length * 2;
            Car[] temp = new Car[newSize];
            for (int i = 0; i < pq.length - 1; i++) {
                temp[i] = pq[i];
            }
            pq = temp;
        }

        if (makeModelMap.get(c.getMake() + c.getModel()) == null) {
            makeModelMap.put(c.getMake() + c.getModel(), c);
        }
        else if (c.getPrice() < makeModelMap.get(c.getMake() + c.getModel()).getPrice()) {
            makeModelMap.replace(c.getMake() + c.getModel(), c);
        }
    }

    protected boolean lesser(int i, int j) {
        if (pq[i] == null || pq[j] == null) {
            return false;
        }
        return (pq[i].getPrice() < pq[j].getPrice());
    }

    protected void replaceMinMakeModel(String makeModel) {
        makeModelMap.remove(makeModel);
        for (int i = 0; i < n; i++) {
            if ((pq[i].getMake() + pq[i].getModel()).equals(makeModel)) {
                if (makeModelMap.get(pq[i].getMake() + pq[i].getModel()) == null) {
                    makeModelMap.put(pq[i].getMake() + pq[i].getModel(), pq[i]);
                }
                else if (pq[i].getPrice() < makeModelMap.get(pq[i].getMake() + pq[i].getModel()).getPrice()) {
                    makeModelMap.replace(pq[i].getMake() + pq[i].getModel(), pq[i]);
                }
            }
        }
    }
}