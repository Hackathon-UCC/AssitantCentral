package me.juan.assistant.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Cities {

    APARTADO("Apartadó"),
    ARAUCA("Arauca"),
    BARRANCABERMEJA("Barrancabermeja"),
    BOGOTA("Bogotá"),
    BUCARAMANGA("Bucaramanga"),
    CALI("Cali"),
    CARTAGO("Cartago"),
    EL_ESPINAL("El Espinal"),
    IBAGUE("Ibagué"),
    MEDELLIN("Medellín"),
    MONTERIA("Montería"),
    NEIVA("Neiva"),
    PASTO("Pasto"),
    PEREIRA("Pereira"),
    POPAYAN("Popayán"),
    QUIBDO("Quibdó"),
    SANTA_MARTA("Santa Marta");

    private static ArrayList<String> cities;
    private final String displayName;

    public static ArrayList<String> getCities() {
        if (cities == null)
            cities = Arrays.stream(Cities.values()).map(Cities::getDisplayName).sorted().collect(Collectors.toCollection(ArrayList::new));
        return cities;
    }
}
