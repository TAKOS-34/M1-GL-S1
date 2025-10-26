public abstract class EltStockage {
    private String nom;
    private int basicSize;
    private Repertoire parent;
    private String adresseAbsolue;

    public EltStockage(String nom, Repertoire parent) {
        this.nom = nom;
        this.parent = parent;

        if (parent == null) {
            if (nom.equals("/")) {
                this.adresseAbsolue = "/";
            } else {
                throw new IllegalArgumentException("Seul le répertoire racine '/' peut avoir un parent null");
            }
        } else {
            if (parent.getAdresseAbsolue().equals("/")) {
                this.adresseAbsolue = "/" + nom;
            } else {
                this.adresseAbsolue = parent.getAdresseAbsolue() + "/" + nom;
            }
        }
    }

    public abstract int size();

    public String getNom() {
        return nom;
    }

    public Repertoire getParent() {
        return parent;
    }

    public String getAdresseAbsolue() {
        return adresseAbsolue;
    }

    protected void setBasicSize(int size) {
        this.basicSize = size;
    }

    protected int getBasicSize() {
        return this.basicSize;
    }
}