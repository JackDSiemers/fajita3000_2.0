package ch.bbw.models;

import javax.validation.constraints.*;

public class Game {

    @NotNull
    @Size(min = 1, max = 16)
    private String name;

    @NotNull
    @Size(min = 1, max = 16)
    private String vorname;

    @NotNull
    @Email(message = "falsch")
    private String email;

    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String telNummer;

    @NotNull
    @Min(12)
    private int alter;
}
