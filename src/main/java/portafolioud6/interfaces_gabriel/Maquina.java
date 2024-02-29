package portafolioud6.interfaces_gabriel;
import com.example.componentecarta.Carta;
import java.util.ArrayList;

public class Maquina {
    private ArrayList<Carta> cartas = new ArrayList<>();
    private int puntos = 0;

    void cartaObtenida(Carta carta) {
        cartas.add(carta);
        puntos += carta.devolverCarta().getValor();
        System.out.println(puntos + " = maquina");
    }

    void reiniciarse() {
        cartas.clear();
        puntos = 0;
    }

    boolean pensar() {
        int valor1 = puntos + 1;
        int valor11 = puntos + 11;
        if ((21 - valor1) < (21 - valor11)) {
            return true;
        } else {
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
