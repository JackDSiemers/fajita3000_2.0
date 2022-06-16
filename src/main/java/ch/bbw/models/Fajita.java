package ch.bbw.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ThreadLocalRandom;

public class Fajita {

    private String id = creatRandomId();

    @NotNull
    @NotBlank
    private int name;

    @NotNull
    @NotBlank
    private String fajitaTortilla;

    private String[] fajitaTortillas = {"corn", "flower"};

    private String[] fajitaVegetableToppings = {"lettuce", "corn", "sour creme", "guacamole", "no vegetables"};

    @NotNull
    @NotBlank
    private String fajitaMeat;

    private String[] fajitaMeats = {"chicken", "spicy chicken", "ground beef", "spicy beef", "vegan chicken", "no meat"};

    private double price = 0;

    public String getId() {
        return id;
    }

    public void setFId(String id) {
        this.id = id;
    }

    public int getFajitasName() {
        return name;
    }

    public void setFajitasName(int name) {
        this.name = name;
    }

    public String getFajitaTortilla() {
        return fajitaTortilla;
    }

    public void setFajitaTortilla(String fajitaTortilla) {
        this.fajitaTortilla = fajitaTortilla;
    }

    public String[] getFajitaTortillas() {
        return fajitaTortillas;
    }

    public void setFajitaTortillas(String[] fajitaTortillas) {
        this.fajitaTortillas = fajitaTortillas;
    }

    public String[] getFajitaVegetableToppings() {
        return fajitaVegetableToppings;
    }

    public void setFajitaVegetableToppings(String[] fajitaVegetableToppings) {
        this.fajitaVegetableToppings = fajitaVegetableToppings;
    }

    public String getFajitaMeat() {
        return fajitaMeat;
    }

    public void setFajitaMeat(String fajitaMeat) {
        this.fajitaMeat = fajitaMeat;
    }

    public String[] getFajitaMeats() {
        return fajitaMeats;
    }

    public void setFajitaMeats(String[] fajitaMeats) {
        this.fajitaMeats = fajitaMeats;
    }

    public double getPrice() {
        calculatePrice();
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private void calculatePrice(){
        setPrice(0);
        switch (this.fajitaMeat) {
            case "chicken":
                this.price += 7;
                break;
            case "spicy chicken":
                this.price += 8.5;
                break;
            case "ground beef":
                this.price += 8;
                break;
            case "spicy beef":
                this.price += 9.5;
                break;
            case "vegan chicken":
                this.price += 10;
                break;
        }
        for(String vegetable : fajitaVegetableToppings){
            this.price += 1;
        }
    }

    private String creatRandomId() {
        String letters = "qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM1234567890";
        String id = "";

        for (int i = 0; i < 15; i++) {
            id += letters.charAt(ThreadLocalRandom.current().nextInt(0, letters.length() + 1));
        }
        return id;
    }
}
