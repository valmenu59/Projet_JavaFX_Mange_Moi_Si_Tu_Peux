package jeu;

public class NbTour {

    private int nbTour;

    public NbTour(){
        this.nbTour = 0;
    }

    public int getNbTour(){
        return this.nbTour;
    }

    public void nouveauTour(){
        this.nbTour+= 1;
    }
}
