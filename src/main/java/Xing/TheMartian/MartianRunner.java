package Xing.TheMartian;

import Xing.TheMartian.domain.Coordinates;
import Xing.TheMartian.domain.Rover;

public class MartianRunner {

    public static void main(String[] args) {
        final MartianService martianService = new MartianService();
        final Coordinates limitCoordinates = new Coordinates.Builder().x(5).y(5).build();
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(3).y(3).build()).orientation('E').build();
        final String commandLine = "MMRMMRMRRM";
        martianService.startMartianApp(limitCoordinates, rover, commandLine);
        System.out.println(rover.toString());
    }

}
