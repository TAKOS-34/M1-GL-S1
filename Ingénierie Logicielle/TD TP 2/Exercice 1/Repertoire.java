import java.util.ArrayList;
import java.util.Collection;

public class Repertoire extends EltStockage implements Element {
    protected ArrayList<EltStockage> elements;

    public Repertoire(String nom, Repertoire parent) {
        super(nom, parent);
        setBasicSize(4);
        this.elements = new ArrayList<>();
    }

    public Repertoire() {
        super("/", null);
        setBasicSize(4);
        this.elements = new ArrayList<>();
    }

    @Override
    public int nbElement() {
        return elements.size();
    }

    @Override
    public int size() {
        int size = 0;

        for (EltStockage e : elements) {
            size += e.size();
        }

        return getBasicSize() + size;
    }

    public void ls() {
        System.out.println("Elements de : " + getNom());
        for (EltStockage e : elements) {
            System.out.println("- " + e.getNom());
        }
        System.out.println("\n");
    }

    public void addElement(EltStockage e) {
        this.elements.add(e);
    }

    public Collection<String> find(String nom) {
        ArrayList<String> adressesTrouvees = new ArrayList<>();

        for (EltStockage e : this.elements) {
            if (e.getNom().equals(nom)) {
                adressesTrouvees.add(e.getAdresseAbsolue());
            }
        }

        return adressesTrouvees;
    }

    public Collection<String> findR(String nom) {
        ArrayList<String> adressesTrouvees = new ArrayList<>();

        for (EltStockage e : this.elements) {
            if (e.getNom().equals(nom)) {
                adressesTrouvees.add(e.getAdresseAbsolue());
            }
            if (e instanceof Repertoire) {
                adressesTrouvees.addAll(((Repertoire) e).findR(nom));
            }
        }

        return adressesTrouvees;
    }
}