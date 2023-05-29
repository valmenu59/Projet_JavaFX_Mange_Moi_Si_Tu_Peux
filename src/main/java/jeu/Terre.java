package jeu;

public class Terre extends Plante {

    private int nbJourPasDePlante;

    public Terre(){
        super("Terre");
        this.nbJourPasDePlante = 0;
    }

    public int getNbJourPasDePlante(){
        return this.nbJourPasDePlante;
    }

    public void pasDePlantePlusUnJour(){
        this.nbJourPasDePlante++;
    }


}
