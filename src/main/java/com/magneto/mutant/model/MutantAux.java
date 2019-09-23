/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magneto.mutant.model;

/**
 *
 * @author Matias Roppel
 */
public class MutantAux {
    private String [] adn;

    public String[] getAdn() {
        return adn;
    }

    public void setAdn(String[] adn) {
        this.adn = adn;
    }

    public String getRawDna(){
        return null;
    }

    public boolean isMutant() {
        //Para las filas (Horizontal)
        int countMutantPatron = 0;
        int count=0;
        int x = 0;
        int y = 1;
        for (String adn1 : adn) {
            if (countMutantPatron<2) {
                while (y < adn1.length() && count<4) {
                    if (adn1.charAt(x) == adn1.charAt(y)) {
                        count++;
                        x++;y++;
                        if(count==3){
                            countMutantPatron++;
                            count=0;
                            x = x - 2;
                            y = y - 2;
                        }
                    } else {
                        x++;y++;
                        count=0;
                    }
                }
                count=0;
                x=0;
                y=1;
            }
        }
        //Para las columnas (Vertical)
        for(int c = 0;c < adn.length;c++){
            if(countMutantPatron<2){
                while(y<adn[c].length() && count<4){
                    if(adn[x].charAt(c)==adn[y].charAt(c)){
                        count++;
                        y++;
                        if(count==3){
                            countMutantPatron++;
                            count=0;
                            y = y - 2;
                        }
                    }else{
                        y++;
                        count=0;
                    }
                }
                count=0;
                x=0;
                y=1;
            }
        }

        //Para las diagonales (Oblicuo)
        for (String adn1 : adn) {
            if (countMutantPatron<2) {
                while (y < adn1.length() && count<4) {
                    if(adn[x].charAt(x)==adn[y].charAt(y)){
                        count++;
                        x++;
                        y++;
                        if(count==3){
                            countMutantPatron++;
                            count=0;
                            x = x - 2;
                            y = y - 2;
                        }
                    }else{
                        count=0;
                        x++;
                        y++;
                    }
                }
                count=0;
                x=0;
                y=1;
            }
        }
        return countMutantPatron > 1;
    }
}
