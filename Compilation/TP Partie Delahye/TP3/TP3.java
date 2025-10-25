import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choisisser entre le graph 1 et 2 : ");
            int graphe = scanner.nextInt();

            System.out.println("Choisisser le nombre de couleur : ");
            int nbCouleurs = scanner.nextInt();

            if (nbCouleurs < 0) {
                System.out.println("Vous avez saisie un k < 0 donc à été remis à 0");
                nbCouleurs = 0;
            }

            switch (graphe) {
                case 1: {
                    graph1(nbCouleurs);
                    break;
                }

                case 2: {
                    graph2(nbCouleurs);
                    break;
                }

                default: {
                    System.out.println("Erreur : Le graph est soit 1 soit 2");
                    break;
                }
            }
        }
    }

    private static List<String> afficherVoisins(List<Noeud> voisins) {
        List<String> noms = new ArrayList<>();
        for (Noeud voisin : voisins) {
            noms.add(voisin.nom);
        }
        return noms;
    }

    private static void afficherResultats(ArrayList<Noeud> sommets) {
        for (Noeud sommet : sommets) {
            System.out.println("Sommet : " + sommet.nom);
            System.out.println("• Connections : " + afficherVoisins(sommet.connections).toString());
            if (!sommet.preferences.isEmpty()) {
                System.out.println("• Préférences : " + afficherVoisins(sommet.preferences).toString());
            }
            if (sommet.getCouleur() == -1) {
                System.out.println("• Couleur : Spillé");
            } else if (sommet.getCouleur() == 0) {
                System.out.println("• Couleur : Non coloré (Erreur possible)");
            } else {
                System.out.println("• Couleur : " + sommet.getCouleur());
            }
            System.out.println("------------------------------------------");
        }
    }

    public static void graph1(int k) {
        Map<String, Noeud> noeuds = new HashMap<>();
        noeuds.put("v", new Noeud("v"));
        noeuds.put("x", new Noeud("x"));
        noeuds.put("z", new Noeud("z"));
        noeuds.put("t", new Noeud("t"));
        noeuds.put("y", new Noeud("y"));
        noeuds.put("u", new Noeud("u"));

        noeuds.get("v").connect(noeuds.get("x"));
        noeuds.get("v").connect(noeuds.get("z"));
        noeuds.get("v").connect(noeuds.get("t"));

        noeuds.get("x").connect(noeuds.get("u"));
        noeuds.get("x").connect(noeuds.get("y"));

        noeuds.get("t").connect(noeuds.get("y"));

        noeuds.get("y").connect(noeuds.get("u"));

        noeuds.get("t").addPreference(noeuds.get("u"));

        ArrayList<String> ordreSommets = new ArrayList<>(Arrays.asList("v", "x", "z", "t", "y", "u"));
        ArrayList<Noeud> sommets = new ArrayList<>();
        for (String nom : ordreSommets) {
            sommets.add(noeuds.get(nom));
        }

        color(k, sommets);
        System.out.println("------------------------------------------");
        afficherResultats(sommets);
        System.out.println("\n\n");
    }

    public static void graph2(int k) {
        Map<String, Noeud> noeuds = new HashMap<>();
        noeuds.put("t", new Noeud("t"));
        noeuds.put("x", new Noeud("x"));
        noeuds.put("y", new Noeud("y"));
        noeuds.put("z", new Noeud("z"));

        noeuds.get("x").connect(noeuds.get("y"));
        noeuds.get("x").connect(noeuds.get("z"));

        noeuds.get("t").connect(noeuds.get("y"));
        noeuds.get("t").connect(noeuds.get("z"));

        ArrayList<String> ordreSommets = new ArrayList<>(Arrays.asList("t", "x", "y", "z"));
        ArrayList<Noeud> sommets = new ArrayList<>();
        for (String nom : ordreSommets) {
            sommets.add(noeuds.get(nom));
        }

        color(k, sommets);
        System.out.println("------------------------------------------");
        afficherResultats(sommets);
        System.out.println("\n\n");
    }

    public static void color(int k, ArrayList<Noeud> sommets) {
        boolean trivialTrouve = false;
        Noeud noeudASimplifier = null;

        for (Noeud sommet : sommets) {
            if (sommet.getDegre() < k && !trivialTrouve) {
                noeudASimplifier = sommet;
                trivialTrouve = true;
                break;
            }
        }

        if (!trivialTrouve && !sommets.isEmpty()) {
            noeudASimplifier = sommets.get(0);
        }

        if (noeudASimplifier != null) {
            ArrayList<Noeud> sommetsRestants = new ArrayList<>(sommets);
            sommetsRestants.remove(noeudASimplifier);

            color(k, sommetsRestants);

            noeudASimplifier.setCouleur(colorierPref(noeudASimplifier, k));
        }
    }

    public static int colorier(Noeud sommet, int k) {
        for (int i = 1; i <= k; i++) {
            boolean couleurDisponible = true;
            for (Noeud voisin : sommet.connections) {
                if (voisin.getCouleur() == i) {
                    couleurDisponible = false;
                    break;
                }
            }
            if (couleurDisponible) {
                return i;
            }
        }
        return -1;
    }

    public static int colorierPref(Noeud sommet, int k) {
        int c = -1;
        ArrayList<Integer> couleursPreferentielles = couleurDisponiblePref(sommet, k);

        for (Integer i : couleursPreferentielles) {
            boolean couleurDisponible = true;
            for (Noeud voisin : sommet.connections) {
                if (voisin.getCouleur() == i) {
                    couleurDisponible = false;
                    break;
                }
            }
            if (couleurDisponible) {
                c = i;
                break;
            }
        }

        if (c == -1) {
            c = colorier(sommet, k);
        }

        return c;
    }

    public static ArrayList<Integer> couleurDisponiblePref(Noeud sommet, Integer k) {
        ArrayList<Integer> couleursDisponibles = new ArrayList<>();

        if (sommet.preferences.isEmpty()) {
            if (sommet.getCouleur() == 0) {
                return couleurDisponible(sommet, k);
            } else if (sommet.getCouleur() > 0) {
                couleursDisponibles.add(sommet.getCouleur());
                return couleursDisponibles;
            } else {
                return couleursDisponibles;
            }
        }

        for (Noeud noeudPrefere : sommet.preferences) {
            if (noeudPrefere.getCouleur() > 0) {
                if (!couleursDisponibles.contains(noeudPrefere.getCouleur())) {
                    couleursDisponibles.add(noeudPrefere.getCouleur());
                }
            } else if (noeudPrefere.getCouleur() == 0) {
                ArrayList<Integer> couleursDuPrefere = couleurDisponiblePref(noeudPrefere, k);
                for (int cPref : couleursDuPrefere) {
                    if (!couleursDisponibles.contains(cPref)) {
                        couleursDisponibles.add(cPref);
                    }
                }
            }
        }
        return couleursDisponibles;
    }

    public static ArrayList<Integer> couleurDisponible(Noeud sommet, Integer k) {
        ArrayList<Integer> couleursDisponibles = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            boolean couleurDisponible = true;
            for (Noeud voisin : sommet.connections) {
                if (voisin.getCouleur() == i) {
                    couleurDisponible = false;
                    break;
                }
            }
            if (couleurDisponible) {
                couleursDisponibles.add(i);
            }
        }
        return couleursDisponibles;
    }
}

class Noeud {
    public String nom;
    public ArrayList<Noeud> connections = new ArrayList<>();
    public ArrayList<Noeud> preferences = new ArrayList<>();
    public int couleur = -1;
    public boolean desactive = false;

    public Noeud(String nom) {
        this.nom = nom;
    }

    public void connect(Noeud n) {
        if (!connections.contains(n)) {
            connections.add(n);
        }
        if (!n.connections.contains(this)) {
            n.connections.add(this);
        }
    }

    public void addPreference(Noeud n) {
        if (!preferences.contains(n)) {
            preferences.add(n);
        }
        if (!n.preferences.contains(this)) {
            n.preferences.add(this);
        }
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public int getDegre() {
        return connections.size();
    }
}