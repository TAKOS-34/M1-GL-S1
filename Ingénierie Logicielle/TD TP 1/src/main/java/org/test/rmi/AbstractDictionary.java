package org.test.rmi;

public abstract class AbstractDictionary implements IDictionary {
    protected Object[] keys;
    protected Object[] values;
    protected int size;

    public AbstractDictionary() {
        this.keys = new Object[0];
        this.values = new Object[0];
        this.size = 0;
    }

    @Override
    public Object get(Object key) {
        int index = indexOf(key);
        return index != -1 ? values[index] : "No value for this key\n";
    }

    @Override
    public boolean containsKey(Object key) {
        return indexOf(key) >= 0;
    }

    @Override
    public IDictionary put(Object key, Object value) {
        if (!this.containsKey(key)) {
            int newIndex = newIndexOf(key);
            this.keys[newIndex] = key;
            this.values[newIndex] = value;
            this.size += 1;
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    public abstract int indexOf(Object key);

    public abstract int newIndexOf(Object key);
}