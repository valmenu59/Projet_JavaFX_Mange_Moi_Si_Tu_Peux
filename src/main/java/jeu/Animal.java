package jeu;

public abstract class Animal {
    
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
