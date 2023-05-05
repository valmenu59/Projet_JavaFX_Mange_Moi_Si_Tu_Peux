package jeu;

public class Plateau {

    private int colonnes;
    private int lignes;

    protected Case[][] cases;

    public Plateau(int l, int c){
        this.lignes = c;
        this.colonnes = l;


    }

    public void creerPlateau(){
        this.cases = new Case[this.lignes][this.colonnes];
        for (int i = 0; i < this.lignes; i++){
            for (int j = 0; j < this.colonnes; j++){

                if (i == 0 || i == this.lignes - 1 || j == 0 || j == this.colonnes - 1) {
                    this.cases[i][j] = new Case(new Roche());
                } else {
                    this.cases[i][j] = new Case(new Herbe());
                }
            }
        }
    }


    public void printMatrices () {
        for (int i = 0; i < this.lignes; i++) {
            for (int j = 0; j < this.colonnes; j++) {
                if (this.cases[i][j].getContenu().getNom().equals("Roche")) {
                    System.out.print("R" + "\t");
                } else {
                    System.out.print("H" + "\t");
                }
            }
            System.out.println("");
        }
    }

    public int getColonnes(){
        return this.colonnes;
    }

    public int getLignes(){
        return this.lignes;
    }

    public void setColonnes(int c){
        this.lignes = c;
    }

    public void setLignes(int l){
        this.lignes = l;
    }

}
