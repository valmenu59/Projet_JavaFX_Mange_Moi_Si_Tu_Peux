package jeu;

public abstract class TypeTerrain {

    String nom;
    boolean mangeable;

    public TypeTerrain(){
        this.nom = "Nom";
        this.mangeable = false;
    }

    public boolean isMangeable() {
        return mangeable;
    }

    public String getNom(){
        return this.nom;
    }


}
