package jeu;

public class Case {

    //private boolean loup;
    //private boolean mouton;
    private TypeTerrain contenu;
    private Animal animal;
    private boolean arrive;

    public Case(TypeTerrain contenu, Animal animal){
        //this.loup = false;
        //this.mouton = false;
        this.contenu = contenu;
        this.animal = animal;
    }

    public TypeTerrain getContenu(){
        return this.contenu;
    }

    public void setContenuGeneral(TypeTerrain c){
        this.contenu = c;
    }

    public void setContenuPlante(Plante p){
        this.contenu = p;
    }

    public void setContenuRandom(){
        double alea = Math.random();
        if (alea <= 0.6){
            this.setContenuPlante(new Herbe());
        } else if (alea <= 0.8) {
            this.setContenuPlante(new Cactus());
        } else {
            this.setContenuPlante(new Marguerite());
        }
    }

    public Animal getAnimal(){
        return this.animal;
    }

    public boolean animalPresent(){
        if (this.animal != null){
            return true;
        } else {
            return false;
        }
    }

    public void setAnimal(Animal a){
        this.animal = a;
    }

    public void removeAnimal(){
        this.animal = null;
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