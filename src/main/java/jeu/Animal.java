package jeu;

import java.io.Serializable;

public abstract class Animal implements Serializable {
    
    private final String nom;

    public Animal(String n){
        this.nom = n;
    }
    
    public String getNom(){
        return this.nom;
    }
}
