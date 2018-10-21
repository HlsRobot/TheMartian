package Xing.TheMartian.service;

import Xing.TheMartian.domain.Coordinates;
import Xing.TheMartian.domain.InputDto;
import Xing.TheMartian.domain.Rover;
import Xing.TheMartian.domain.RoverCommand;
import Xing.TheMartian.enums.Command;
import Xing.TheMartian.enums.Orientation;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputService {

    private BufferedReader bufferedReader;

    public InputService(final BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }


    /**
     * Public method called from the main to provide the initial input
     *
     * @return InputDto         The plateaus limits and a list of rovers and commands for each rover
     * @throws IOException       because the BufferedReader can throw one
     */
    public InputDto provideInitialInput() throws IOException {
        List<RoverCommand> roverCommandList = new ArrayList<>();
        boolean provideMoreRovers = true;
        System.out.println("Welcome to Mars.");
        System.out.println("****************************");
        System.out.println("Please input the max coordinates of the plateau (E.g. 5 5):");
        final Coordinates limitCoordinates = this.handleMaxCoordinatesInput(this.bufferedReader);
        while (provideMoreRovers) {
            System.out.println("Please input the initial coordinates and orientation of the rover (E.g. 5 5 N):");
            final Rover rover = this.handleRoverCoordinatesInput(this.bufferedReader, limitCoordinates);
            System.out.println("Please input the commands in one line (e.g. LMLMRMMR)");
            final String commandLine = this.handleCommandInput(this.bufferedReader);
            roverCommandList.add(new RoverCommand.Builder().rover(rover).commandLine(commandLine).build());
            System.out.println("Provide more rovers? (type Y for yes or anything else for no)");
            provideMoreRovers = this.handleQuestion(this.bufferedReader);
        }
        return new InputDto.Builder().limitCoordinates(limitCoordinates).roverCommandList(roverCommandList).build();
    }

    /**
     * Method that handles the user input of the limit coordinates from the console
     *
     * @param br                BufferedReader
     * @return Coordinates      the limit coordinates of the plateau.
     * @throws IOException      in case of IO error
     */
    private Coordinates handleMaxCoordinatesInput(final BufferedReader br) throws IOException {
        final String input = br.readLine();
        try {
            String[] parts = input.split(" ");
            int[] coordinates = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
            if (coordinates.length != 2) {
                System.err.println("Please provide exactly 2 coordinates (e.g. 5 5).");
                return this.handleMaxCoordinatesInput(br);
            }
            for (int coordinate: coordinates) {
                if (coordinate < 1 ) {
                    System.err.println("Please provide a value larger than 0");
                    return this.handleMaxCoordinatesInput(br);
                }
            }
            return new Coordinates.Builder().x(coordinates[0]).y(coordinates[1]).build();
        } catch (NumberFormatException nfe) {
            System.err.println("Please provide Integer values larger than 0 whitespace separated (e.g. 5 5).");
            return this.handleMaxCoordinatesInput(br);
        }
    }

    /**
     * Method that handles the user input of the rover coordinates and orientation from the console
     *
     * @param br                BufferedReader
     * @param limitCoordinates  Max value of the initial rover coordinates
     * @return Rover            the limit coordinates of the plateau.
     * @throws IOException      in case of IO error
     */
    private Rover handleRoverCoordinatesInput(final BufferedReader br, final Coordinates limitCoordinates) throws IOException {
        final String input = br.readLine();
        try {
            String[] parts = input.split(" ");
            if (parts.length != 3) {
                System.err.println("Please provide the correct amount of parameters (e.g. 5 5 N).");
                return this.handleRoverCoordinatesInput(br, limitCoordinates);
            }
            final Coordinates coordinates = new Coordinates.Builder().x(Integer.parseInt(parts[0]))
                    .y(Integer.parseInt(parts[1])).build();
            if (coordinates.getX() > limitCoordinates.getX() || coordinates.getY() > limitCoordinates.getY()) {
                System.err.println(String.format(
                        "please provide coordinates inside the limits of the plateau (Max value: (%d, %d))",
                        limitCoordinates.getX(), limitCoordinates.getY()));
                return this.handleRoverCoordinatesInput(br, limitCoordinates);
            }

            if (parts[2].length() != 1 || (
                    parts[2].charAt(0) != Orientation.NORTH.getOrientationCode()
                            && parts[2].charAt(0) != Orientation.SOUTH.getOrientationCode()
                            && parts[2].charAt(0) != Orientation.WEST.getOrientationCode()
                            && parts[2].charAt(0) != Orientation.EAST.getOrientationCode())) {
                System.err.println("Please provide a correct orientation. Allowed values: N, W, S, E.");
                return this.handleRoverCoordinatesInput(br, limitCoordinates);
            }
            final char orientation = parts[2].charAt(0);
            return new Rover.Builder().coordinates(coordinates).orientation(Orientation.getByCode(orientation)).build();
        } catch (NumberFormatException nfe) {
            System.err.println("Please provide integer values larger than 0 whitespace separated followed by the orientation (e.g. 5 5 N).");
            return this.handleRoverCoordinatesInput(br, limitCoordinates);
        }
    }

    /**
     * Method that handles the user input of the command line from the console
     *
     * @param br                BufferedReader
     * @return String           cleaned up command line
     * @throws IOException      in case of IO error
     */
    private String handleCommandInput(final BufferedReader br) throws IOException {
        final String input = br.readLine();
        StringBuilder fixedCommands = new StringBuilder();
        for (char character: input.toCharArray()) {
            char upperChar = Character.toUpperCase(character);
            if (upperChar == Command.MOVE.getCommandCode() || upperChar == Command.RIGHT.getCommandCode()
                    || upperChar == Command.LEFT.getCommandCode()) {
                fixedCommands.append(upperChar);
            } else {
                System.err.println(String.format("Character %c is not a valid command character thus removed from the command line", character));
            }
        }
        return fixedCommands.toString();
    }

    /**
     * Method that handles the user input of a simple question
     *
     * @param br                BufferedReader
     * @return boolean          If we should keep providing rovers
     * @throws IOException      in case of IO error
     */
    private boolean handleQuestion(final BufferedReader br) throws IOException {
        final String input = br.readLine();
        return input.toUpperCase().equals("Y");
    }
}
