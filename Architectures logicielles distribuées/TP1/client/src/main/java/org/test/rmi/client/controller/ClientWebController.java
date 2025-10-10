package org.test.rmi.client.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Import
import org.test.rmi.client.impl.Chat;
import org.test.rmi.common.interfaces.CabinetService;
import java.rmi.RemoteException;

@Controller
public class ClientWebController {

    @Autowired
    private CabinetService cabinetService;

    @GetMapping("/")
    public String index(Model model) throws RemoteException {
        model.addAttribute("animaux", cabinetService.getAllAnimals());
        model.addAttribute("especes", cabinetService.getAllEspeces());
        return "index";
    }

    @PostMapping("/addAnimal")
    public String addAnimal(
            @RequestParam String nom, @RequestParam String nomMaitre,
            @RequestParam String race, @RequestParam String espece,
            RedirectAttributes redirectAttributes) throws RemoteException {
        cabinetService.addAnimal(nom, nomMaitre, race, espece);
        redirectAttributes.addFlashAttribute("successMessage", "L'animal '" + nom + "' a été ajouté avec succès !");
        return "redirect:/";
    }

    @PostMapping("/addChat")
    public String addChat(
            @RequestParam String nom, @RequestParam String nomMaitre,
            @RequestParam String race, @RequestParam String couleurPelage,
            RedirectAttributes redirectAttributes) throws RemoteException {
        Chat chat = new Chat(nom, nomMaitre, race, couleurPelage);
        cabinetService.addAnimalSpecial(chat);
        redirectAttributes.addFlashAttribute("successMessage", "Le chat '" + nom + "' a été ajouté avec succès !");
        return "redirect:/";
    }

    @PostMapping("/addEspece")
    public String addEspece(
            @RequestParam String nom, @RequestParam int dureeVie,
            RedirectAttributes redirectAttributes) throws RemoteException {
        cabinetService.addEspece(nom, dureeVie);
        redirectAttributes.addFlashAttribute("successMessage", "L'espèce '" + nom + "' a été ajoutée avec succès !");
        return "redirect:/";
    }

    @PostMapping("/deleteAnimal")
    public String deleteAnimal(@RequestParam String nom, RedirectAttributes redirectAttributes) throws RemoteException {
        cabinetService.deleteAnimalByNom(nom);
        redirectAttributes.addFlashAttribute("successMessage", "L'animal '" + nom + "' a été supprimé.");
        return "redirect:/";
    }

    // NOUVELLE MÉTHODE POUR SUPPRIMER UNE ESPÈCE
    @PostMapping("/deleteEspece")
    public String deleteEspece(@RequestParam String nom, RedirectAttributes redirectAttributes) throws RemoteException {
        String serverResponse = cabinetService.deleteEspeceByNom(nom);
        // On affiche soit le message de succès, soit le message d'erreur du serveur
        if (serverResponse.startsWith("Erreur")) {
            redirectAttributes.addFlashAttribute("errorMessage", serverResponse); // (A améliorer avec une zone d'erreur dans le HTML)
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "L'espèce '" + nom + "' a été supprimée.");
        }
        return "redirect:/";
    }

    @PostMapping("/updateDossier")
    public String updateDossier(
            @RequestParam String nom, @RequestParam String dossier,
            RedirectAttributes redirectAttributes) throws RemoteException {
        cabinetService.setDossier(nom, dossier);
        redirectAttributes.addFlashAttribute("successMessage", "Le dossier de l'animal '" + nom + "' a été mis à jour.");
        return "redirect:/";
    }
}