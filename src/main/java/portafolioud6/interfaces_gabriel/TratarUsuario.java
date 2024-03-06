package portafolioud6.interfaces_gabriel;

public final class TratarUsuario {

    private String usuario;

    private final static TratarUsuario INSTANCIA = new TratarUsuario();

    private TratarUsuario() {
    }


    public static TratarUsuario obtenerInstancia() {
        return INSTANCIA;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
