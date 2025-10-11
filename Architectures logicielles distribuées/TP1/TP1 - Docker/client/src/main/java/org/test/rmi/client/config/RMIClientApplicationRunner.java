package org.test.rmi.client.config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;
import org.test.rmi.client.impl.Alerte;
import org.test.rmi.client.impl.Chat;
import org.test.rmi.common.impl.Espece;
import org.test.rmi.common.interfaces.CabinetService;
import java.util.ArrayList;
import java.util.Scanner;

// @Component
public class RMIClientApplicationRunner implements ApplicationRunner {
    private RmiProxyFactoryBean proxy;

    public RMIClientApplicationRunner(RmiProxyFactoryBean proxy) {
        this.proxy = proxy;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CabinetService service = (CabinetService) proxy.getObject();
        Scanner scanner = new Scanner(System.in);
        Alerte alerte = new Alerte();
        service.addAlerte(alerte);

        while (true) {
            System.out.print(
                    "---------------------------------------------------------\n"+
                    "> Menu :\n" +
                        "- 1 : Ajouter un animal\n" +
                        "- 2 : Ajouter une espèce\n" +
                        "- 3 : Ajouter un chat\n\n" +
                        "- 4 : Chercher un animal par nom\n" +
                        "- 5 : Chercher une espèce par nom\n" +
                        "- 6 : Chercher un dossier par nom\n\n" +
                        "- 7 : Récupérer tous les animaux\n" +
                        "- 8 : Récupérer toutes les espcèces\n" +
                        "- 9 : Récupérer tous les dossiers\n\n" +
                        "- 10 : Supprimer un animal\n" +
                        "- 11 : Supprimer une espèce\n\n" +
                        "- 12 : Mettre a jour un dossier\n\n" +
                        "- 13 : Se retirer de la liste d'alerte\n" +
                        "- 14 : Se mettre dans la liste d'alerte\n\n" +
                        "- quit : Sortir du programme\n" +
                    "---------------------------------------------------------\n\n> "
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

                    String res = service.addAnimal(nom, nomMaitre, race, espece);
                    System.out.println(res + "\n");
                    break;
                }

                case "2": {
                    System.out.print("Nom de l'espèce : ");
                    String nom = scanner.nextLine();

                    System.out.print("Durée de vie moyenne (en années > 0) : ");
                    int dureeVie;
                    try {
                        dureeVie = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("\nDurée de vie doit être un entier\n");
                        break;
                    }
                    String res = service.addEspece(nom, dureeVie);
                    System.out.println(res + "\n");
                    break;
                }

                case "3": {
                    System.out.print("Nom de l'espèce : ");
                    String nom = scanner.nextLine();

                    System.out.print("Nom du maître : ");
                    String nomMaitre = scanner.nextLine();

                    System.out.print("Race : ");
                    String race = scanner.nextLine();

                    System.out.print("Couleur du pelage : ");
                    String couleurPelage = scanner.nextLine();

                    Chat chat = new Chat(nom, nomMaitre, race, couleurPelage);
                    String res = service.addAnimalSpecial(chat);
                    System.out.println(res + "\n");
                    break;
                }

                case "4": {
                    System.out.print("Nom de l'animal à chercher : ");
                    String nom = scanner.nextLine();

                    String res = service.getAnimalByNom(nom);
                    System.out.print("\n");
                    System.out.println(res);
                    System.out.print("\n");
                    break;
                }

                case "5": {
                    System.out.print("Nom de l'espèce à chercher : ");
                    String nom = scanner.nextLine();

                    Espece res = service.getEspeceByNom(nom);
                    System.out.print("\n");
                    if (res != null) {
                        System.out.println(res.formatedToString());
                    } else {
                        System.out.println("Erreur : aucuns espèces avec ce nom n'existe");
                    }
                    System.out.print("\n");
                    break;
                }

                case "6": {
                    System.out.print("Nom du dossier à chercher : ");
                    String nom = scanner.nextLine();

                    String res = service.getDossierByNom(nom);
                    System.out.print("\n");
                    System.out.println(res);
                    System.out.print("\n");
                    break;
                }

                case "7": {
                    ArrayList<String> res = service.getAllAnimals();
                    System.out.print("\n");
                    if (!res.isEmpty()) {
                        for (int i = 0; i < res.size(); i++) {
                            System.out.println("Animal n°" + (i+1) + " : " + res.get(i) + "\n");
                        }
                    } else {
                        System.out.println("Aucuns animales disponibles\n");
                    }
                    break;
                }

                case "8": {
                    ArrayList<String> res = service.getAllEspeces();
                    System.out.print("\n");
                    if (!res.isEmpty()) {
                        for (int i = 0; i < res.size(); i++) {
                            System.out.println("Espece n°" + (i+1) + " : " + res.get(i) + "\n");
                        }
                    } else {
                        System.out.println("Aucunes espèces disponibles\n");
                    }
                    break;
                }

                case "9": {
                    ArrayList<String> res = service.getAllDossiers();
                    System.out.print("\n");
                    if (!res.isEmpty()) {
                        for (int i = 0; i < res.size(); i++) {
                            System.out.println("Dossier n°" + (i+1) + " : " + res.get(i) + "\n");
                        }
                        System.out.print("\n");
                    } else {
                        System.out.println("Aucuns dossiers disponibles\n");
                    }
                    break;
                }

                case "10": {
                    System.out.print("Nom de l'animal à supprimer : ");
                    String nom = scanner.nextLine();

                    String res = service.deleteAnimalByNom(nom);
                    System.out.println(res + "\n");
                    break;
                }

                case "11": {
                    System.out.print("Nom de l'espèce à supprimer : ");
                    String nom = scanner.nextLine();

                    String res = service.deleteEspeceByNom(nom);
                    System.out.println(res + "\n");
                    break;
                }

                case "12": {
                    System.out.print("Nom de l'animal dont le dossier est a mettre a jour : ");
                    String nom = scanner.nextLine();

                    System.out.print("Nouveau contenu du dossier : ");
                    String dossier = scanner.nextLine();

                    String res = service.setDossier(nom, dossier);
                    System.out.println(res + "\n");
                    break;
                }

                case "13": {
                    String res = service.deleteAlerte(alerte);
                    System.out.println(res + "\n");
                    break;
                }

                case "14": {
                    String res = service.addAlerte(alerte);
                    System.out.println(res + "\n");
                    break;
                }

                case "quit": {
                        service.deleteAlerte(alerte);
                        System.out.println("Bye!\n");
                        scanner.close();
                        System.exit(0);
                        break;
                    }

                default:
                    System.out.println("Commande non valide\n");
            }
        }
    }
}