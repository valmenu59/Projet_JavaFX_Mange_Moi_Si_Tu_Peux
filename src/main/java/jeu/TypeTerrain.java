package jeu;

import java.io.Serializable;

public abstract class TypeTerrain implements Serializable {

    private String nom;

    public TypeTerrain(String n){
        this.nom = n;
    }



    public String getNom(){
        return this.nom;
    }


}
