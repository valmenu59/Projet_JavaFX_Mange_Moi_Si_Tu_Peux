package jeu;

public class Terre extends Plante {

    private int nbJourVide;

    public Terre(){
        super("Terre", false);
        this.nbJourVide = 0;
    }

    public int getNbJourVide(){
        return this.nbJourVide;
    }


}
