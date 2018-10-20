package Xing.TheMartian.domain;

import Xing.TheMartian.enums.Command;
import Xing.TheMartian.enums.Orientation;

import java.util.logging.Logger;

public class Rover {

    private static final Logger LOGGER = Logger.getLogger( Rover.class.getName() );

    private Coordinates coordinates;
    private char orientation;

    private Rover(final Coordinates coordinates, final char orientation) {
        this.coordinates = coordinates;
        this.orientation = orientation;
    }

    public Rover() {}

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public char getOrientation() {
        return orientation;
    }

    public void changeOrientation(final Command command) {
        final char currentOrientation = Orientation.adjustOrientation(this.orientation, command).getOrientationCode();
        if (currentOrientation == 'U') {
            LOGGER.warning("Due to harsh weather conditions the orientation was lost. Defaulting to NORTH");
            this.orientation = Orientation.NORTH.getOrientationCode();
        }
        this.orientation = currentOrientation;
    }

    public void moveNorth(final int maxY) {
        if (this.coordinates.getY() + 1 > maxY) {
            LOGGER.warning("The obstacle avoidance module detected that we cannot move to the North");
        } else {
            this.coordinates.setY(this.coordinates.getY() + 1);
        }
    }

    public void moveSouth() {
        if (this.coordinates.getY() - 1 < 0) {
            LOGGER.warning("The obstacle avoidance module detected that we cannot move to the South");
        } else {
            this.coordinates.setY(this.coordinates.getY() - 1);
        }
    }

    public void moveEast(final int maxX) {
        if (this.coordinates.getX() + 1 > maxX) {
            LOGGER.warning("The obstacle avoidance module detected that we cannot move to the East");
        } else {
            this.coordinates.setX(this.coordinates.getX() + 1);
        }
    }

    public void moveWest() {
        if (this.coordinates.getX() - 1 < 0) {
            LOGGER.warning("The obstacle avoidance module detected that we cannot move to the West");
        } else {
            this.coordinates.setX(this.coordinates.getX() - 1);
        }
    }

    @Override
    public String toString() {
        return this.getCoordinates().getX() + " " + this.getCoordinates().getY() + " " + this.getOrientation();
    }

    public static class Builder {
        private Coordinates coordinates;
        private char orientation;

        public Builder coordinates(final Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder orientation(final char orientation) {
            this.orientation = orientation;
            return this;
        }

        public Rover build() {
            return new Rover(this.coordinates, this.orientation);
        }
    }
}
