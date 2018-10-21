package Xing.TheMartian.service;

import Xing.TheMartian.domain.Coordinates;
import Xing.TheMartian.domain.InputDto;
import Xing.TheMartian.domain.Rover;
import Xing.TheMartian.domain.RoverCommand;
import Xing.TheMartian.enums.Orientation;
import Xing.TheMartian.service.MartianService;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MartianServiceTest {

    private MartianService martianService;

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        LogManager.getRootLogger().addAppender(mockAppender);
        this.martianService = new MartianService();
    }

    @Test
    public void startMartianAppMultipleRoversSuccessTest() {
        final List<RoverCommand> roverCommandList = new ArrayList<>();
        roverCommandList.add(new RoverCommand.Builder().
                rover(new Rover.Builder()
                        .coordinates(new Coordinates.Builder().x(1).y(2).build())
                        .orientation(Orientation.NORTH).build())
                .commandLine("LMLMLMLMM").build());
        roverCommandList.add(new RoverCommand.Builder().
                rover(new Rover.Builder()
                        .coordinates(new Coordinates.Builder().x(3).y(3).build())
                        .orientation(Orientation.EAST).build())
                .commandLine("MMRMMRMRRM").build());
        final InputDto inputDto = new InputDto.Builder()
                .limitCoordinates(new Coordinates.Builder().x(5).y(5).build())
                .roverCommandList(roverCommandList)
                .build();
        final List<Rover> roverList = this.martianService.startMartianApp(inputDto);
        assertEquals(2, roverList.size());
        assertEquals(1, roverList.get(0).getCoordinates().getX());
        assertEquals(3, roverList.get(0).getCoordinates().getY());
        assertEquals(Orientation.NORTH, roverList.get(0).getOrientation());
        assertEquals(5, roverList.get(1).getCoordinates().getX());
        assertEquals(1, roverList.get(1).getCoordinates().getY());
        assertEquals(Orientation.EAST, roverList.get(1).getOrientation());
    }

    @Test
    public void startMartianAppWithFaultyCommandsTest() {
        final InputDto inputDto = this.inputDtoFactory(6, 6, 1, 2, Orientation.NORTH, "abcLMLMLMLMM");
        this.martianService.startMartianApp(inputDto);
        verify(this.mockAppender, times(3)).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> loggingEventList = captorLoggingEvent.getAllValues();
        assertTrue(loggingEventList.stream().anyMatch(
                loggingEvent -> loggingEvent.getRenderedMessage().contains("Command a was not recognized and was ignored")));
        assertTrue(loggingEventList.stream().anyMatch(
                loggingEvent -> loggingEvent.getRenderedMessage().contains("Command b was not recognized and was ignored")));
        assertTrue(loggingEventList.stream().anyMatch(
                loggingEvent -> loggingEvent.getRenderedMessage().contains("Command c was not recognized and was ignored")));
    }

    @Test
    public void moveRoverNorthFailTest() {
        final InputDto inputDto = this.inputDtoFactory(5, 5, 0, 5, Orientation.NORTH, "M");
        final List<Rover> roverList = this.martianService.startMartianApp(inputDto);
        verify(this.mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getRenderedMessage().contains("The obstacle avoidance module detected that we cannot move to the North"));
        assertEquals(inputDto.getRoverCommandList().get(0).getRover().getCoordinates().getY(), roverList.get(0).getCoordinates().getY());
    }

    @Test
    public void moveRoverSouthFailTest() {
        final InputDto inputDto = this.inputDtoFactory(5, 5, 0, 0, Orientation.SOUTH, "M");
        final List<Rover> roverList = this.martianService.startMartianApp(inputDto);
        verify(this.mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getRenderedMessage().contains("The obstacle avoidance module detected that we cannot move to the South"));
        assertEquals(inputDto.getRoverCommandList().get(0).getRover().getCoordinates().getY(), roverList.get(0).getCoordinates().getY());
    }

    @Test
    public void moveRoverEastFailTest() {
        final InputDto inputDto = this.inputDtoFactory(5, 5, 5, 0, Orientation.EAST, "M");
        final List<Rover> roverList = this.martianService.startMartianApp(inputDto);
        verify(this.mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getRenderedMessage().contains("The obstacle avoidance module detected that we cannot move to the East"));
        assertEquals(inputDto.getRoverCommandList().get(0).getRover().getCoordinates().getX(), roverList.get(0).getCoordinates().getX());
    }

    @Test
    public void moveRoverWestFailTest() {
        final InputDto inputDto = this.inputDtoFactory(5, 5, 0, 0, Orientation.WEST, "M");
        final List<Rover> roverList = this.martianService.startMartianApp(inputDto);
        verify(this.mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertTrue(loggingEvent.getRenderedMessage().contains("The obstacle avoidance module detected that we cannot move to the West"));
        assertEquals(inputDto.getRoverCommandList().get(0).getRover().getCoordinates().getX(), roverList.get(0).getCoordinates().getX());
    }

    private InputDto inputDtoFactory(final int maxX, final int maxY, final int roverX, final int roverY,
                                     final Orientation orientation, final String commands) {
        return new InputDto.Builder()
                .limitCoordinates(new Coordinates.Builder().x(maxX).y(maxY).build())
                .roverCommandList(Collections.singletonList(
                        new RoverCommand.Builder().
                                rover(new Rover.Builder()
                                        .coordinates(new Coordinates.Builder().x(roverX).y(roverY).build())
                                        .orientation(orientation).build())
                                .commandLine(commands).build()))
                .build();
    }
}