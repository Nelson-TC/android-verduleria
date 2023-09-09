package com.example.verdumarket.utils;

public class MeasurementUtils {

    public enum UnitMeasurement {
        PIECE("Pieza"),
        BUNCH("Manojo"),
        GRAM("Gramo"),
        KILOGRAM("Kilogramo"),
        POUND("Libra"),
        OUNCE("Onza"),
        LITER("Litro"),
        MILLILITER("Mililitro"),
        PINT("Pinta"),
        QUART("Cuarto"),
        GALLON("Galon"),
        MILLIMETER("Milímetro"),
        CENTIMETER("Centímetro"),
        METER("Metro"),
        INCH("Pulgada"),
        FOOT("Pie");

        private final String displayName;

        UnitMeasurement(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


}
