package de.l3s.hits.shs.client;

public enum Direction {
    FORWARD(0),
    BACKWARD(1),
    /**
     * Alias for FORWARD
     */
    OUTGOING(0),
    /**
     * Alias for BACKWARD
     */
    INCOMING(1);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
