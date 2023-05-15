package jeu;

public abstract class TypeTerrain {

    private String nom;

    public TypeTerrain(String n){
        this.nom = n;
    }



    public String getNom(){
        return this.nom;
    }


}
