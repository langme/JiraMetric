package com.langme.newmetrics.dummy;

import com.langme.newmetrics.Constantes;

import java.io.Serializable;

/**
 * Created by melang on 12/07/2017.
 */

public class ResumeItem implements Serializable{
    private String name;
    private int nb;

    public ResumeItem(String nameItem, int nombre){
        this.name = nameItem;
        this.nb = nombre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }
}
