package org.test.soap.server.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Annotation pour définir l'entité JPA
@Entity
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Identifiant de l'employé

    private String name; // Nom de l'employé
    
    // Constructeurs
    public Employee() {
		
	}
    
    public Employee(String name) {
        this.name = name;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}