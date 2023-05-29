package jeu;

import java.io.Serializable;

public class Case implements Serializable {

    private TypeTerrain contenu;
    private Animal animal;


    public Case(TypeTerrain contenu, Animal animal){
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

    public void setPlanteRandom(){
        double alea = Math.random();
        if (alea <= 0.5){
            this.contenu = new Herbe();
        } else if (alea <= 0.75) {
            this.contenu = new Cactus();
        } else {
            this.contenu = new Marguerite();
        }
    }

    public Animal getAnimal(){
        return this.animal;
    }

    public boolean animalPresent(){
        return this.animal != null;
    }

    public void setAnimal(Animal a){
        this.animal = a;
    }

    public void removeAnimal(){
        this.animal = null;
    }







}