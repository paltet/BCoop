package com.bcoop.bcoop.Model;

import java.util.List;

public class HabilitatDetall {
    private int valoracio;
    private List<Comentari> comentaris;

    public HabilitatDetall() {
        this.valoracio = 0;
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
}
