package portafolioud6.interfaces_gabriel;

import com.example.componentecarta.Carta;
import com.example.componentecarta.CartaObjeto;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;

import java.util.SplittableRandom;

public class Baraja {
    private Carta[] baraja;

    public Baraja() {
        this.baraja = rellenarBaraja();
    }

    private Carta[] rellenarBaraja() {

        try {

            Carta[] barajaLLena = new Carta[52];

            String[] palos = {"clubs", "diamonds", "hearts", "spades"};

            String[] cartas = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

            int contador = 0;

            for (int j = 0; j < palos.length; j++) {

                for (int k = 0; k < cartas.length; k++) {

                    CartaObjeto cartaObjeto = new CartaObjeto(palos[j], cartas[k]);

                    if (cartas[k].equals("ace")) {
                        cartaObjeto.setValor(1);
                    } else if (cartas[k].equals("jack") || cartas[k].equals("queen") || cartas[k].equals("king")) {
                        cartaObjeto.setValor(10);
                    } else {
                        cartaObjeto.setValor(Integer.parseInt(cartas[k]));
                    }

                    cartaObjeto.setImg(obtenerImagen(cartas[k], palos[j]));

                    Carta carta = new Carta(cartaObjeto);

                    barajaLLena[contador] = carta;

                    contador++;
                }

            }
            return barajaLLena;
        } catch (Exception e) {
            System.out.println("excepcion");
            e.printStackTrace();
        }
        return null;
    }

    public Carta[] getBaraja() {
        return baraja;
    }

    private Image obtenerImagen(String carta, String palo) {

        String formatoImagen = carta + "_of_" + palo;

        if (carta.equals("king") || carta.equals("queen") || carta.equals("jack")) {
            return new Image(getClass().getResourceAsStream("baraja/" + formatoImagen + "2.png"));
        } else {
            return new Image(getClass().getResourceAsStream("baraja/" + formatoImagen + ".png"));
        }
    }
}

