public class Lien extends ElementAtomique {
    protected Fichier fichierLie;

    public Lien(String nom, Repertoire parent, Fichier fichierLie) {
        super(nom, parent);
        this.fichierLie = fichierLie;
        setBasicSize(0);
    }

    @Override
    public int size() {
        return fichierLie.size();
    }

    @Override
    public String cat() {
        return fichierLie.cat();
    }
}