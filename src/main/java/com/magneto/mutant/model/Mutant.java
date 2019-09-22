package com.magneto.mutant.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Mutant implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String adn;
    private int mutante;

    public Mutant() {
    }

    public Mutant(int id, String adn, int mutante) {
        this.id = id;
        this.adn = adn;
        this.mutante = mutante;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    
    
    public String getAdn() {
        return adn;
    }

    public void setAdn(String adn) {
        this.adn = adn;
    }

    public int getMutante() {
        return mutante;
    }

    public void setMutante(int mutante) {
        this.mutante = mutante;
    }  

}
