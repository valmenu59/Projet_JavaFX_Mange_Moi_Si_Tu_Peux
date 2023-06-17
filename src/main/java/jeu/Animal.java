/*
 * Copyright (c)  @link https://github.com/valmenu59/SAE_JavaFX @link https://github.com/valmenu59
 * CC BY-NC - Attribution - Partage dans les mêmes conditions - Pas d'utilisation commerciale
 */

package jeu;

import java.io.Serializable;

public abstract class Animal implements Serializable {
    
    private final String nom;

    public Animal(String n){
        this.nom = n;
    }
    
    public String getNom(){
        return this.nom;
    }
}
