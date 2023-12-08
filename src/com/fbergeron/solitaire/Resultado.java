package com.fbergeron.solitaire;

import java.util.Comparator;

public class Resultado {
    private String tempo;
    private int movimentos;

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public int getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(int movimentos) {
        this.movimentos = movimentos;
    }
    
    public static Comparator<Resultado> getComparatorByTempo() {
        return new Comparator<Resultado>() {
            @Override
            public int compare(Resultado resultado1, Resultado resultado2) {
                String[] tempo1Parts = resultado1.getTempo().split(":");
                String[] tempo2Parts = resultado2.getTempo().split(":");
                
                int horas1 = Integer.parseInt(tempo1Parts[0]);
                int minutos1 = Integer.parseInt(tempo1Parts[1]);
                int horas2 = Integer.parseInt(tempo2Parts[0]);
                int minutos2 = Integer.parseInt(tempo2Parts[1]);
                
                if (horas1 < horas2) {
                    return -1;
                } else if (horas1 > horas2) {
                    return 1;
                } else {
                    return Integer.compare(minutos1, minutos2);
                }
            }
        };
    }
}