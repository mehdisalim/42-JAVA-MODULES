package fr.fortytwo.reflection.classes;

import java.util.StringJoiner;

public class Car {

    private String name;
    private String type;
    private int version;

    public Car() {
    }

    public Car(final String name, final String type, final Integer version) {
        this.name = name;
        this.type = type;
        this.version = version;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("version=" + version)
                .toString();
    }

}