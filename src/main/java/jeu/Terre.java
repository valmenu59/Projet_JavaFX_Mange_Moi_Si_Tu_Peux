/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

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
