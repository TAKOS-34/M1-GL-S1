package org.test.rmi.client.config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;
import org.test.rmi.common.interfaces.CabinetService;
import java.util.ArrayList;
import java.util.Scanner;

@Component
public class RMIClientApplicationRunner implements ApplicationRunner {
    private RmiProxyFactoryBean proxy;

    public RMIClientApplicationRunner(RmiProxyFactoryBean proxy) {
        this.proxy = proxy;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CabinetService service = (CabinetService) proxy.getObject();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(
                    "> Commandes disponibles :\n" +
                        "- \"1\" : Ajouter un animal\n" +
                        "- \"2\" : Ajouter une espèce\n" +
                        "- \"3\" : Ajouter un dossier\n\n" +
                        "- \"4\" : Chercher un animal par nom\n\n" +
                        "- \"5\" : Récupérer tous les animaux\n\n" +
                        "- \"exit\" : Sortir du programme\n"
            );

            switch (scanner.nextLine().toLowerCase()) {
                case "1": {
                    System.out.print("Nom de l'animal : ");
                    String nom = scanner.nextLine();

                    System.out.print("Nom du maître : ");
                    String nomMaitre = scanner.nextLine();

                    System.out.print("Race : ");
                    String race = scanner.nextLine();

                    System.out.print("Nom de l'espèce : ");
                    String espece = scanner.nextLine();

                    System.out.print("Nom du dossier : ");
                    String dossier = scanner.nextLine();

                    boolean ok = service.addAnimal(nom, nomMaitre, race, espece, dossier);
                    System.out.println(ok ? "Animal ajouté" : "Erreur lors de l'ajout");
                    break;
                }

                case "2": {
                    System.out.print("Nom de l'espèce : ");
                    String nomEspece = scanner.nextLine();

                    System.out.print("Durée de vie moyenne (en années) : ");
                    int dureeVie = Integer.parseInt(scanner.nextLine());

                    boolean ok = service.addEspece(nomEspece, dureeVie);
                    System.out.println(ok ? "Espèce ajoutée" : "Erreur lors de l'ajout.");
                    break;
                }

                case "3": {
                    System.out.print("Nom du dossier : ");
                    String nomDossier = scanner.nextLine();

                    boolean ok = service.addDossier(nomDossier);
                    System.out.println(ok ? "Dossier ajouté" : "Erreur lors de l'ajout");
                    break;
                }

                case "4": {
                    System.out.print("Nom de l'animal à chercher : ");
                    String nomRecherche = scanner.nextLine();

                    String result = service.getAnimalByNom(nomRecherche);
                    System.out.println("Résultat : " + result);
                    break;
                }

                case "5": {
                    ArrayList<String> result = service.getAllAnimals();
                    for (int i = 0; i < result.size(); i++) {
                        System.out.println("Animal n°" + (i+1) + " : " + result.get(i) + "\n");
                    }
                    break;
                }

                case "exit":
                    System.out.println("Bye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Commande non valide");
            }
        }
    }
}