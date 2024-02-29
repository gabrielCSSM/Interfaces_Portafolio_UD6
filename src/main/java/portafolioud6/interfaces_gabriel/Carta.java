package portafolioud6.interfaces_gabriel;

import javafx.scene.image.Image;

public class Carta {
    private int valor;
    private Image img;
    private String formatoImagen;
    private String palo;
    private String carta;
    private boolean descubierta;

    public Carta(String palo, String carta) {
        this.carta = carta;
        this.palo = palo;
        this.formatoImagen = carta + "_of_" + palo;
        if (carta.equals("king") || carta.equals("queen") || carta.equals("jack")) {
            this.img = new Image(getClass().getResourceAsStream("baraja/" + formatoImagen + "2.png"));
        } else {
            this.img = new Image(getClass().getResourceAsStream("baraja/" + formatoImagen + ".png"));
        }

    }
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public String getFormatoImagen() {
        return formatoImagen;
    }

    public void setFormatoImagen(String formatoImagen) {
        this.formatoImagen = formatoImagen;
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

    public String getCarta() {
        return carta;
    }

    public void setCarta(String carta) {
        this.carta = carta;
    }

    public boolean isDescubierta() {
        return descubierta;
    }

    public void setDescubierta(boolean descubierta) {
        this.descubierta = descubierta;
    }

    @Override
    public String toString() {
        return carta + " de " + palo;
    }
}