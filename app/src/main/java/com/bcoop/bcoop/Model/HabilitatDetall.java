package com.bcoop.bcoop.Model;

import java.util.ArrayList;
import java.util.List;

public class HabilitatDetall {
    private int valoracio;
    private List<Comentari> comentaris;
    private int numeroValoracions;
    private int sumaTotalValoracions;

    public HabilitatDetall() {
        this.valoracio = 0;
        comentaris = new ArrayList<>();
        this.numeroValoracions = 0;
        this.sumaTotalValoracions = 0;

    }

    public HabilitatDetall(int valoracio, List<Comentari> comentaris) {
        this.valoracio = valoracio;
        this.comentaris = comentaris;
    }

    public int getValoracio() {
        return valoracio;
    }

    public void setValoracio(int valoracio) {
        this.valoracio = valoracio;
    }

    public List<Comentari> getComentaris() {
        return comentaris;
    }

    public void setComentaris(List<Comentari> comentaris) {
        this.comentaris = comentaris;
    }

    public int getNumeroValoracions() {
        return numeroValoracions;
    }

    public void setNumeroValoracions(int numeroValoracions) {
        this.numeroValoracions = numeroValoracions;
    }

    public int getSumaTotalValoracions() {
        return sumaTotalValoracions;
    }

    public void setSumaTotalValoracions(int sumaTotalValoracions) {
        this.sumaTotalValoracions = sumaTotalValoracions;
    }

}
