package portafolioud6.interfaces_gabriel;

import java.util.ArrayList;
import java.util.Random;

public class Casa {
    private ArrayList<Carta> cartas = new ArrayList<>();
    private int puntos = 0;

    void cartaObtenida(Carta carta) {
        cartas.add(carta);
        puntos += carta.getValor();
    }

    void reiniciarse() {
        cartas.clear();
        puntos = 0;
    }

    boolean pensar() {
        int valor1 = puntos + 1;
        int valor11 = puntos + 11;
        if ((21 - valor1) < (21 - valor11)) {
            System.out.println("valer 1 mejor");
            return true;
        } else {
            System.out.println("valer 11 mejor");
            return false;
        }
    }
    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
