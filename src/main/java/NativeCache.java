import java.lang.reflect.Array;

public class NativeCache<T> {

    public int size;
    public int step;
    public int numberEmptySlots;
    public String [] slots;
    public T [] values;
    public int [] hits;
    public int minimumHits;
    public int minimumHitsIndex;

    public NativeCache(int sz, Class clazz) {
        size = sz;
        step = size;
        numberEmptySlots = size;
        slots = new String[size];
        for (int i = 0; i < size; i += 1) {
            slots[i] = null;
        }
        values = (T[]) Array.newInstance(clazz, this.size);
        hits = new int[size];
        for (int i = 0; i < size; i += 1) {
            hits[i] = 0;
        }
    }

    public int hashFun(String key) {
        return key.getBytes().length % size;
    }

    public boolean isKey(String key) {
        if (find(key) == -1) {
            return false;
        }
        return true;
    }

    public void put(String key, T value) {
        int slotIndex = -1;

        if (!isKey(key)) {
            while (slotIndex == -1) {
                slotIndex = seekSlot(key);
                if (slotIndex == -1) {
                    freeSpace();
                }
            }
            slots[slotIndex] = key;
            numberEmptySlots -= 1;
            values[slotIndex] = value;
            minimumHits = hits[slotIndex];
            minimumHitsIndex = slotIndex;
        }
    }

    public void freeSpace() {
        slots[minimumHitsIndex] = null;
        hits[minimumHitsIndex] = 0;
        numberEmptySlots += 1;
    }

    public int seekSlot(String key) {
        int slotIndex = hashFun(key);

        for (int variableStep = step; variableStep > 0 && numberEmptySlots != 0; variableStep -= 1) {
            for (int i = slotIndex, circleNumber = 0; i < size && circleNumber < step;
                 circleNumber = (i + variableStep < size ? circleNumber : circleNumber + 1),
                         i = (i + variableStep < size ? i + variableStep : i + variableStep - size)) {

                if (slots[i] == null) {
                    return i;
                }
                if (i == slotIndex && circleNumber != 0) {
                    variableStep -= 1;
                }
            }
        }
        return -1;
    }

    public T get(String key) {
        int slotIndex = find(key);

        if (slotIndex != -1) {
            hits[slotIndex] += 1;
            if (minimumHitsIndex == slotIndex) {
                minimumHits += 1;

                for (int i = 0; i < size && numberEmptySlots == 0; i += 1) {
                    if (hits[i] < minimumHits) {
                        minimumHits = hits[i];
                        minimumHitsIndex = i;
                        break;
                    }
                }
            }
            return values[slotIndex];
        }
        return null;
    }

    public int find(String key) {
        int slotIndex = hashFun(key);

        for (int variableStep = step; variableStep > 0; variableStep -= 1) {
            for (int i = slotIndex, circleNumber = 0; i < size && circleNumber < step;
                 circleNumber = (i + variableStep < size ? circleNumber : circleNumber + 1),
                         i = (i + variableStep < size ? i + variableStep : i + variableStep - size)) {

                if (key.equals(slots[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
}