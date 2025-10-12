    package org.test.rmi.server.impl;
    import org.springframework.stereotype.Service;
    import org.test.rmi.common.interfaces.DossierService;

    public class Dossier implements DossierService {
        private String nom;

        public Dossier() {}

        public Dossier(String nom) {
            this.nom = "Dossier_" + nom;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String formatedToString() {
            return nom;
        }
    }
