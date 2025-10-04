package org.test.rmi;

public class FastDictionary extends AbstractDictionary {
    public FastDictionary() {}

    @Override
    public int indexOf(Object key) {
        for (int i = 0; i < this.keys.length; i++) {
            if (this.keys[i] != null && this.keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public int newHashCode(Object key) {
        return Math.abs(key.hashCode());
    }

    @Override
    public int newIndexOf(Object key) {
        this.grow();
        int index = newHashCode(key) % this.keys.length;

        while (keys[index] != null) {
            index = (index + 1) % this.keys.length;
        }

        return index;
    }

    boolean mustGrow() {
        return this.size() >= (this.keys.length * 3) / 4;
    }

    void grow() {
        if (this.mustGrow()) {
            int size = this.size() > 0 ? this.keys.length : 1;

            Object[] newKeys = new Object[size * 2];
            Object[] newValues = new Object[size * 2];

            for (int i = 0; i < this.keys.length; i++) {
                newKeys[i] = this.keys[i];
                newValues[i] = this.values[i];
            }

            this.keys = newKeys;
            this.values = newValues;
        }
    }
}