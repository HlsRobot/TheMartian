package Xing.TheMartian.enums;

import com.sun.org.apache.xpath.internal.operations.Or;

public enum Orientation {
    NORTH('N'), WEST('W'), SOUTH('S'), EAST('E'), UNKNOWN('U');

    private final char orientationCode;

    Orientation(char orientationCode) {
        this.orientationCode = orientationCode;
    }

    public char getOrientationCode() {
        return this.orientationCode;
    }

    public static Orientation getByCode(char code) {
        for(Orientation orientation : values()) {
            if(orientation.orientationCode == code) {
                return orientation;
            }
        }
        return UNKNOWN;
    }

    public static Orientation adjustOrientation(final char initialOrientation, final Command command) {
        switch (Orientation.getByCode(initialOrientation)) {
            case NORTH:
                if (command == Command.RIGHT) {
                    return EAST;
                }
                return WEST;
            case WEST:
                if (command == Command.RIGHT) {
                    return NORTH;
                }
                return SOUTH;
            case SOUTH:
                if (command == Command.RIGHT) {
                    return WEST;
                }
                return EAST;
            case EAST:
                if (command == Command.RIGHT) {
                    return SOUTH;
                }
                return NORTH;
            default:
                return UNKNOWN;
        }
    }
}
