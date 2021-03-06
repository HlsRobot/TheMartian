package Xing.TheMartian.domain;

import Xing.TheMartian.enums.Command;
import Xing.TheMartian.enums.Orientation;
import org.apache.log4j.Logger;


public class Rover {

    private static final Logger LOGGER = Logger.getLogger(Rover.class);

    private Coordinates coordinates;
    private Orientation orientation;

    private Rover(final Coordinates coordinates, final Orientation orientation) {
        this.coordinates = coordinates;
        this.orientation = orientation;
    }

    public Rover() {}

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void changeOrientation(final Command command) {
        final Orientation currentOrientation = Orientation.adjustOrientation(this.orientation, command);
        if (currentOrientation == Orientation.UNKNOWN) {
            LOGGER.warn("Due to harsh weather conditions the orientation was lost. Defaulting to NORTH");
            this.orientation = Orientation.NORTH;
        } else {
            this.orientation = currentOrientation;
        }
    }

    public void moveNorth() {
        this.coordinates.setY(this.coordinates.getY() + 1);
    }

    public void moveSouth() {
        this.coordinates.setY(this.coordinates.getY() - 1);
    }

    public void moveEast() {
        this.coordinates.setX(this.coordinates.getX() + 1);
    }

    public void moveWest() {
        this.coordinates.setX(this.coordinates.getX() - 1);
    }

    @Override
    public String toString() {
        return this.getCoordinates().getX() + " " + this.getCoordinates().getY() + " " + this.getOrientation().getOrientationCode();
    }

    public static class Builder {
        private Coordinates coordinates;
        private Orientation orientation;

        public Builder coordinates(final Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder orientation(final Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public Rover build() {
            return new Rover(this.coordinates, this.orientation);
        }
    }
}
