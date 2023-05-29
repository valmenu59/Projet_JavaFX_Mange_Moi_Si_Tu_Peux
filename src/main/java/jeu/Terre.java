package jeu;

public class Terre extends Plante {

    private int nbJourPasDePlante;

    public Terre(){
        super("Terre", false);
        this.nbJourPasDePlante = 0;
    }

    public int getNbJourPasDePlante(){
        return this.nbJourPasDePlante;
    }

    public void pasDePlantePlusUnJour(){
        System.out.println("Il n'y a pas de plante depuis : "+nbJourPasDePlante);
        this.nbJourPasDePlante++;
    }


}
