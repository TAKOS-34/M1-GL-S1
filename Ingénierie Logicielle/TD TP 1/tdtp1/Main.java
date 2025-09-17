public class Main {
    public static void main(String[] args) {
        OrderedDictionary d1 = new OrderedDictionary();

        System.out.println(d1.isEmpty());

        d1.put("Dorian", "Battlefield6");
        System.out.println(d1.indexOf("Dorian"));
        System.out.println(d1.containsKey("Dorian"));
        System.out.println(d1.newIndexOf("Dorian2"));
        System.out.println(d1.get("Dorian"));

        System.out.println(d1.indexOf("Dorian2"));
        System.out.println(d1.containsKey("Dorian2"));
        System.out.println(d1.newIndexOf("Dorian"));
        System.out.println(d1.get("Dorian2"));
    }
}
