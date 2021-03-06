package Xing.TheMartian.service;

import Xing.TheMartian.domain.InputDto;
import Xing.TheMartian.domain.RoverCommand;
import Xing.TheMartian.enums.Command;
import Xing.TheMartian.domain.Coordinates;
import Xing.TheMartian.domain.Rover;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class MartianService {

    private static final Logger LOGGER = Logger.getLogger(MartianService.class);

    /**
     * Method that is called from the runner and is initializing the logic
     *
     * @param inputDto      The plateaus limits and a list of rovers and commands for each rover
     */
    public List<Rover> startMartianApp(final InputDto inputDto) {
        final List<Rover> finalRoverList = new ArrayList<>();
        for (final RoverCommand roverCommand: inputDto.getRoverCommandList()) {
            this.executeCommands(inputDto.getLimitCoordinates(), roverCommand.getRover(), roverCommand.getCommandLine());
            finalRoverList.add(roverCommand.getRover());
        }
        return finalRoverList;
    }

    /**
     * Method that executes the commands one after the other
     *
     * @param limitCoordinates          The plateaus limits
     * @param rover                     The initial coordinates of the rover and its orientation
     * @param commandLine               A string of commands for the rover
     */
    private void executeCommands(final Coordinates limitCoordinates, final Rover rover, final String commandLine) {
        final char[] commands = commandLine.toCharArray();

        for (char command : commands) {
            if (this.isValidCommand(command)) {
                if (command == Command.MOVE.getCommandCode()) {
                    this.moveRover(rover, limitCoordinates);
                } else {
                    rover.changeOrientation(Command.getByCode(command));
                }
            } else {
                LOGGER.warn(String.format("Command %c was not recognized and was ignored", command));
            }
        }
    }

    private boolean isValidCommand(final char command) {
        return command == 'R' || command == 'M' || command == 'L';
    }

    private void moveRover(final Rover rover, final Coordinates limitCoordinates) {
        switch (rover.getOrientation()) {
            case NORTH:
                if (rover.getCoordinates().getY() + 1 > limitCoordinates.getY()) {
                    LOGGER.warn("The obstacle avoidance module detected that we cannot move to the North");
                } else {
                    rover.moveNorth();
                }
                break;
            case WEST:
                if (rover.getCoordinates().getX() - 1 < 0) {
                    LOGGER.warn("The obstacle avoidance module detected that we cannot move to the West");
                } else {
                    rover.moveWest();
                }
                break;
            case SOUTH:
                if (rover.getCoordinates().getY() - 1 < 0) {
                    LOGGER.warn("The obstacle avoidance module detected that we cannot move to the South");
                } else {
                    rover.moveSouth();
                }
                break;
            case EAST:
                if (rover.getCoordinates().getX() + 1 > limitCoordinates.getX()) {
                    LOGGER.warn("The obstacle avoidance module detected that we cannot move to the East");
                } else {
                    rover.moveEast();
                }
                break;
            default:
                break;
        }
    }
}
