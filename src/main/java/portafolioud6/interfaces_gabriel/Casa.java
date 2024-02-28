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
