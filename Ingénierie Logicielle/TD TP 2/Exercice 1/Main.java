public class Main {
    public static void main(String[] args) {
        try {
            // Création de la racine
            Repertoire racine = new Repertoire(); // Utilise le constructeur "/"
            System.out.println("Racine créée : " + racine.getAdresseAbsolue());

            // Création de répertoires
            Repertoire home = new Repertoire("home", racine);
            Repertoire user1 = new Repertoire("user1", home);
            Repertoire user2 = new Repertoire("user2", home);
            Repertoire documents = new Repertoire("documents", user1);
            racine.addElement(home);
            home.addElement(user1);
            home.addElement(user2);
            user1.addElement(documents);

            // Création de fichiers
            Fichier f1 = new Fichier("notes.txt", user1, "Ceci est un fichier de notes.");
            Fichier f2 = new Fichier("rapport.pdf", documents, "Contenu du rapport PDF. Beaucoup de mots.");
            Fichier f3 = new Fichier("config.ini", home, "user=admin");
            user1.addElement(f1);
            documents.addElement(f2);
            home.addElement(f3);

            // Création d'un lien
            Lien lienRapport = new Lien("lien_rapport", user2, f2);
            user2.addElement(lienRapport);

            // Test de la structure
            System.out.println("\n--- Contenu de la racine ---");
            racine.ls();

            System.out.println("--- Contenu de /home ---");
            home.ls();

            System.out.println("--- Contenu de /home/user1 ---");
            user1.ls();

            System.out.println("--- Contenu de /home/user1/documents ---");
            documents.ls();

            System.out.println("--- Contenu de /home/user2 ---");
            user2.ls();

            // Test des adresses absolues
            System.out.println("\n--- Adresses absolues ---");
            System.out.println("Racine : " + racine.getAdresseAbsolue());
            System.out.println("Home : " + home.getAdresseAbsolue());
            System.out.println("User1 : " + user1.getAdresseAbsolue());
            System.out.println("Fichier f2 : " + f2.getAdresseAbsolue());
            System.out.println("Lien : " + lienRapport.getAdresseAbsolue());

            // Test de la taille (size)
            System.out.println("\n--- Tailles (size) ---");
            System.out.println("Taille f1 (notes.txt) : " + f1.size() + " (Attendu: " + "Ceci est un fichier de notes.".length() + ")");
            System.out.println("Taille f2 (rapport.pdf) : " + f2.size() + " (Attendu: " + "Contenu du rapport PDF. Beaucoup de mots.".length() + ")");
            System.out.println("Taille lienRapport (points sur f2) : " + lienRapport.size() + " (Attendu: " + f2.size() + ")");
            System.out.println("Taille /home/user1/documents : " + documents.size() + " (Attendu: 4 + " + f2.size() + ")");
            System.out.println("Taille /home/user1 : " + user1.size() + " (Attendu: 4 + " + f1.size() + " + " + documents.size() + ")");
            System.out.println("Taille /home : " + home.size() + " (Attendu: 4 + " + user1.size() + " + " + user2.size() + " + " + f3.size() + ")");
            System.out.println("Taille / (racine) : " + racine.size() + " (Attendu: 4 + " + home.size() + ")");

            // Test de nbElement
            System.out.println("\n--- Nombre d'éléments (nbElement) ---");
            System.out.println("nbElement f1 (notes.txt) : " + f1.nbElement() + " (Nb espaces, Attendu: 5)");
            System.out.println("nbElement f2 (rapport.pdf) : " + f2.nbElement() + " (Nb espaces, Attendu: 5)");
            System.out.println("nbElement /home/user1 : " + user1.nbElement() + " (Nb EltStockage, Attendu: 2)");
            System.out.println("nbElement / : " + racine.nbElement() + " (Nb EltStockage, Attendu: 1)");

            // Test de cat (contenu)
            System.out.println("\n--- Contenu (cat) ---");
            System.out.println("cat f1 : " + f1.cat());
            System.out.println("cat lienRapport : " + lienRapport.cat());

            // Test de find (recherche non récursive)
            System.out.println("\n--- Recherche (find) ---");
            System.out.println("find 'user1' dans /home : " + home.find("user1"));
            System.out.println("find 'rapport.pdf' dans /home : " + home.find("rapport.pdf")); // Ne doit rien trouver
            System.out.println("find 'rapport.pdf' dans /home/user1/documents : " + documents.find("rapport.pdf"));

            // Test de findR (recherche récursive)
            // Ajoutons un élément conflictuel pour tester findR
            Fichier conflit = new Fichier("notes.txt", documents, "Autre notes");
            documents.addElement(conflit);

            System.out.println("\n--- Recherche récursive (findR) ---");
            System.out.println("findR 'notes.txt' dans /home : " + home.findR("notes.txt"));
            System.out.println("findR 'rapport.pdf' dans / : " + racine.findR("rapport.pdf"));
            System.out.println("findR 'lien_rapport' dans /home : " + home.findR("lien_rapport"));
            System.out.println("findR 'config.ini' dans / : " + racine.findR("config.ini"));

            // Test d'erreur (parent null pour autre chose que la racine)
            try {
                System.out.println("\n--- Test d'erreur (IllegalArgumentException) ---");
                new Repertoire("test_erreur", null);
            } catch (IllegalArgumentException e) {
                System.out.println("Exception attrapée (normal) : " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Une erreur inattendue est survenue : ");
            e.printStackTrace();
        }
    }
}