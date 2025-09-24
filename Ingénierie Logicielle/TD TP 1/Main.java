public class Main {
    public static void main(String[] args) {
        OrderedDictionary d1 = new OrderedDictionary();

        boolean orderedDictionary = false;
        if (orderedDictionary) {
            d1.put("alice", "dupont");
            d1.put("bob", "martin");
            d1.put("carla", "legrand");
            d1.put("emma", "durand");
            d1.put("lucas", "bernard");
            d1.put("zoe", "petit");
            d1.put("max", "girard");
            d1.put("leo", "robert");
            d1.put("nina", "moreau");

            System.out.println(d1.isEmpty());
            System.out.println(d1.size());
            System.out.println(d1.get("dorian"));
            System.out.println(d1.get("salut"));
            System.out.println(d1.indexOf("dorian"));
            System.out.println(d1.indexOf("salut"));
            System.out.println(d1.containsKey("dorian"));
            System.out.println(d1.containsKey("salut"));
            System.out.println(d1.isEmpty());
            System.out.println(d1.size());
        }

        FastDictionary d2 = new FastDictionary();
        boolean fastDictionary = false;
        if (fastDictionary) {
            d2.put("alice", "dupont");
            d2.put("bob", "martin");
            d2.put("carla", "legrand");
            d2.put("emma", "durand");
            d2.put("lucas", "bernard");
            d2.put("zoe", "petit");
            d2.put("max", "girard");
            d2.put("leo", "robert");
            d2.put("nina", "moreau");
            d2.put("alice", "dupont"); // doublon

            System.out.println(d2.get("alice"));
            System.out.println(d2.get("bob"));
            System.out.println(d2.get("carla"));
            System.out.println(d2.get("emma"));
            System.out.println(d2.get("lucas"));
            System.out.println(d2.get("zoe"));
            System.out.println(d2.get("max"));
            System.out.println(d2.get("leo"));
            System.out.println(d2.get("nina"));
            System.out.println(d2.get("test"));
        }

        SortedDictionary d3 = new SortedDictionary();
        boolean sortedDictionary = true;
        if (sortedDictionary) {
            d3.put("j", "juliet");
            d3.put("a", "alpha");
            d3.put("m", "mike");
            d3.put("d", "delta");
            d3.put("g", "golf");
            d3.put("b", "beta");
            d3.put("z", "zulu");
            d3.put("e", "echo");
            d3.put("k", "kilo");
            d3.put("c", "charlie");
            d3.put("f", "foxtrot");

            d3.afficher();
    }
}
}