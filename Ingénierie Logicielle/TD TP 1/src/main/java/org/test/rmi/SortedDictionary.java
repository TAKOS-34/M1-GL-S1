package org.test.rmi;

public class SortedDictionary extends AbstractDictionary {
    public SortedDictionary() {}

    @Override
    public int indexOf(Object key) {
        if (!(key instanceof Comparable)) throw new IllegalArgumentException("Incorrect type of key\n");
        for (int i = 0; i < this.keys.length; i++) {
            if (this.keys[i] != null && this.keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int newIndexOf(Object key) {
        int size = this.size();
        int newIndex = size;

        Object[] newKeys = new Object[size + 1];
        Object[] newValues = new Object[size + 1];

        for (int i = 0; i < this.keys.length; i++) {
            if (((Comparable) keys[i]).compareTo(key) > 0) {
                newIndex = i;
                break;
            }
        }
        
        for (int i = 0; i < newIndex; i++) {
            newKeys[i] = this.keys[i];
            newValues[i] = this.values[i];
        }

        for (int i = newIndex; i < size; i++) {
            newKeys[i + 1] = this.keys[i];
            newValues[i + 1] = this.values[i];
        }

        this.keys = newKeys;
        this.values = newValues;

        return newIndex;
    }

    public Object getDichotomie(Object key) {
        int low = 0;
        int high = this.size - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Comparable midKey = (Comparable) this.keys[mid];

            if (midKey.equals(key)) {
                return this.values[mid];
            } else if (midKey.compareTo(key) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return "No value for this key\n";
    }

    public void afficher() {
        for (int i = 0; i < this.keys.length; i++) {
            System.out.println(this.keys[i]);
        }
    }
}