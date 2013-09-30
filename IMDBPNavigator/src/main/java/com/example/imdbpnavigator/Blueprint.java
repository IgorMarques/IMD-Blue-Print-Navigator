package com.example.imdbpnavigator;


public class Blueprint {

    private int id;
    private String name;
    private String path;

    public Blueprint(){
        super();
    }

    public Blueprint(int id, String name, String path) {
        super();
        this.id = id;
        this.name = name;
        this.path  = path;
    }

    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }


}
