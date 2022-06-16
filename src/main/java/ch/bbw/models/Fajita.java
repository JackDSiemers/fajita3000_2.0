package ch.bbw.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Fajita {

    private int id;

    private int name;

    @NotNull
    @NotBlank
    private String fajitaSize;

    private String[] fajitaSizes = {"Normal", "large"};

    private String[] vegetables = {"tomatoes", "lettuce", "corn", "onions"};

    private double price = 0;

}
