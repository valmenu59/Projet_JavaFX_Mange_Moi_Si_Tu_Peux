package jeu;

public class Terre extends TypeTerrain {

    private int nbJourVide;

    public Terre(){
        this.nom = "Terre";
        this.mangeable = false;
        this.nbJourVide = 0;
    }

    public int getNbJourVide(){
        return this.nbJourVide;
    }


}
