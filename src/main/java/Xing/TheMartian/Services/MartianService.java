package Xing.TheMartian.Services;

import Xing.TheMartian.domain.InputDto;
import Xing.TheMartian.domain.RoverCommand;
import Xing.TheMartian.enums.Command;
import Xing.TheMartian.domain.Coordinates;
import Xing.TheMartian.domain.Rover;

import java.util.logging.Logger;

public class MartianService {

    private static final Logger LOGGER = Logger.getLogger( MartianService.class.getName() );

    /**
     * Method that is called from the runner and is initializing the logic
     *
     * @param inputDto      The plateaus limits and a list of rovers and commands for each rover
     */
    public void startMartianApp(final InputDto inputDto) {
        for (final RoverCommand roverCommand: inputDto.getRoverCommandList()) {
            this.executeCommands(inputDto.getLimitCoordinates(), roverCommand.getRover(), roverCommand.getCommandLine());
            System.out.println(roverCommand.getRover());
        }
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
                LOGGER.warning(String.format("Command %c was not recognized and was ignored", command));
            }
        }
    }

    private boolean isValidCommand(final char command) {
        return command == 'R' || command == 'M' || command == 'L';
    }

    private void moveRover(final Rover rover, final Coordinates limitCoordinates) {
        switch (rover.getOrientation()) {
            case NORTH:
                rover.moveNorth(limitCoordinates.getY());
                break;
            case WEST:
                rover.moveWest();
                break;
            case SOUTH:
                rover.moveSouth();
                break;
            case EAST:
                rover.moveEast(limitCoordinates.getX());
                break;
            default:
                break;
        }
    }
}
