package me.kitty.radon.Utils;

public class DataUtils {

    public static Enum<?> next(Enum<?> current) {

        Enum<?>[] values = current.getDeclaringClass().getEnumConstants();
        int index = current.ordinal();

        return values[(index + 1) % values.length];

    }

}
