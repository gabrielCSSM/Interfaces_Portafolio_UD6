package portafolioud6.interfaces_gabriel;

import javafx.fxml.Initializable;

import java.util.SplittableRandom;

public class Baraja {
    private Carta[] baraja;

    public Baraja() {
        this.baraja = rellenarBaraja();
    }

    private Carta[] rellenarBaraja() {

        Carta[] barajaLLena = new Carta[52];
        String[] palos = {"clubs", "diamonds", "hearts", "spades"};
        String[] cartas = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
        int contador = 0;

        for (int j = 0; j < palos.length; j++) {

            for (int k = 0; k < cartas.length; k++) {

                Carta c = new Carta(palos[j], cartas[k]);

                if (cartas[k].equals("ace")) {
                    c.setValor(1);
                } else if (cartas[k].equals("jack") || cartas[k].equals("queen") || cartas[k].equals("king")) {
                    c.setValor(10);
                } else {
                    c.setValor(Integer.parseInt(c.getCarta()));
                }

                barajaLLena[contador] = c;

                contador++;
            }
        }

        return barajaLLena;
    }

    public Carta[] getBaraja() {
        return baraja;
    }

}
