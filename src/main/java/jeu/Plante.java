package jeu;

public abstract class  Plante extends TypeTerrain {

    private boolean mangeable;
    boolean loupEstLa;
    boolean moutonEstLa;

    public Plante(String n, boolean m){
        super(n);
        this.mangeable = m;
    }

    public boolean isMangeable() {
        return mangeable;
    }
}
