package jeu;

public class Case {

    private boolean loup;
    private boolean mouton;
    private TypeTerrain contenu;
    private boolean arrive;

    public Case(TypeTerrain contenu){
        this.loup = false;
        this.mouton = false;
        this.contenu = contenu;
    }

    public TypeTerrain getContenu(){
        return this.contenu;
    }

    public void setContenu(TypeTerrain c){
        this.contenu = c;
    }


    public TypeTerrain nouvellePlante(){
        double nbr = Math.random();
        if (nbr <= 0.60){
            return new Herbe();
        } else if (nbr <= 0.80){
            return new Cactus();
        } else {
            return new Marguerite();
        }
    }




}