package jeu;

import java.io.Serializable;

public abstract class TypeTerrain implements Serializable {

    private final String nom;

    public TypeTerrain(String n){
        this.nom = n;
    }

    public String getNom(){
        return this.nom;
    }


}
