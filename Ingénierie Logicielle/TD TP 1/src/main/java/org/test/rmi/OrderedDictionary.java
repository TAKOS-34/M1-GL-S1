package org.test.rmi;

public class OrderedDictionary extends AbstractDictionary {
    public OrderedDictionary() {}

    @Override
    public int indexOf(Object key) {
        for (int i = 0; i < this.keys.length; i++) {
            if (this.keys[i] != null && this.keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int newIndexOf(Object key) {
        int newIndex = this.keys.length;

        Object[] newKeys = new Object[newIndex + 1];
        Object[] newValues = new Object[newIndex + 1];

        for (int i = 0; i < this.keys.length; i++) {
            newKeys[i] = this.keys[i];
            newValues[i] = this.values[i];
        }

        this.keys = newKeys;
        this.values = newValues;

        return newIndex;
    }
}