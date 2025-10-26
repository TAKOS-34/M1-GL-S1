public abstract class ElementAtomique extends EltStockage {
    public ElementAtomique(String nom, Repertoire parent) {
        super(nom, parent);
        setBasicSize(0);
    }

    public abstract String cat();
}