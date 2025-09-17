public class OrderedDictionary extends AbstractDictionary {
    public OrderedDictionary() {}

    @Override
    public int indexOf(Object key) {
        for (int i = 0; i < this.size; i++) {
            if (this.keys.get(i).equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int newIndexOf(Object key) {
        if (!this.containsKey(key)) {
            return this.size;
        }
        return -1;
    }
}