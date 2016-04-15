package com.punisher.fitnesstracker.util;

/**
 * A simple implementation of a Ternary based on the Ternary Numerical System.
 */
public final class Ternary {

    public static int FALSE = 0;
    public static int TRUE = 1;
    public static int UNDEFINED = 2;

    private int _currentValue;

    public Ternary() {
        _currentValue = FALSE;
    }

    public Ternary(int v) {
        setValue(v);
    }

    public Ternary setValue(int v) {
        if (v < 0 || v > 2) {
            throw new RuntimeException("Values between 0 and 2 are acceptable");
        }
        _currentValue = v;
        return this;
    }

    public int getValue() {
        return _currentValue;
    }

    @Override
    public int hashCode() {
        return _currentValue;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null) && (o instanceof Ternary) && ((Ternary)o).getValue() == _currentValue;
    }

    @Override
    public String toString() {
        if (_currentValue == FALSE) {
            return "false";
        }
        if (_currentValue == TRUE) {
            return "true";
        }
        if (_currentValue == UNDEFINED) {
            return "Undefined";
        }

        throw new RuntimeException("Value is not valid: " + _currentValue);
    }
}
