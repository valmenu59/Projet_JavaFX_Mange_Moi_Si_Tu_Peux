package jeu;

import java.io.Serializable;

public abstract class Animal implements Serializable {
    
    private String nom;
    private boolean passif;
    private int nbDeplacement;

    public Animal(String n, int depl){
        this.nom = n;
        this.passif = true;
        this.nbDeplacement = depl;
    }
    
    public String getNom(){
        return this.nom;
    }
}
