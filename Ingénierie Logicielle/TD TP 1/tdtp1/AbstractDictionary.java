import java.util.ArrayList;

public abstract class AbstractDictionary implements IDictionary {
    protected ArrayList<Object> keys;
    protected ArrayList<Object> values;
    protected int size;

    public AbstractDictionary() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
        this.size = 0;
    }

    @Override
    public Object get(Object key) {
        for (int i = 0; i < this.size; i++) {
            if (keys.get(i).equals(key)) {
                return values.get(i);
            }
        }
        return null;
    }

    @Override
    public IDictionary put(Object key, Object value) {
        if (!this.containsKey(key)) {
            int newIndex = newIndexOf(key);
            this.keys.add(newIndex, key);
            this.values.add(newIndex, value);
            this.size++;
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < this.size; i++) {
            if (keys.get(i).equals(key)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    public abstract int indexOf(Object key);

    public abstract int newIndexOf(Object key);
}