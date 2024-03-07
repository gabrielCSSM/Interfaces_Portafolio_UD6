package portafolioud6.interfaces_gabriel;

import java.util.ArrayList;
import java.util.List;

public class TratarPuntuaciones {

    private List<Jugador> jugadores;

    private final static TratarPuntuaciones INSTANCIA = new TratarPuntuaciones();

    private TratarPuntuaciones() {
    }


    public static TratarPuntuaciones obtenerInstancia() {
        return INSTANCIA;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugador) {
        this.jugadores = jugador;
    }
}
