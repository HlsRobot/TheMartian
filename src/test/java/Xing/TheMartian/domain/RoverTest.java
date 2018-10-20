package Xing.TheMartian.domain;

import Xing.TheMartian.enums.Command;
import Xing.TheMartian.enums.Orientation;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoverTest {

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        LogManager.getRootLogger().addAppender(mockAppender);
    }

    @Test
    public void changeOrientationSuccessTest() {
        final Rover rover = new Rover.Builder().orientation(Orientation.NORTH).build();
        rover.changeOrientation(Command.RIGHT);
        assertSame(rover.getOrientation(), Orientation.EAST);
        rover.changeOrientation(Command.RIGHT);
        assertSame(rover.getOrientation(), Orientation.SOUTH);
        rover.changeOrientation(Command.RIGHT);
        assertSame(rover.getOrientation(), Orientation.WEST);
        rover.changeOrientation(Command.RIGHT);
        assertSame(rover.getOrientation(), Orientation.NORTH);
        rover.changeOrientation(Command.LEFT);
        assertSame(rover.getOrientation(), Orientation.WEST);
        rover.changeOrientation(Command.LEFT);
        assertSame(rover.getOrientation(), Orientation.SOUTH);
        rover.changeOrientation(Command.LEFT);
        assertSame(rover.getOrientation(), Orientation.EAST);
        rover.changeOrientation(Command.LEFT);
        assertSame(rover.getOrientation(), Orientation.NORTH);
    }

    @Test
    public void changeOrientationFailTest() {
        final Rover rover = new Rover.Builder().orientation(Orientation.UNKNOWN).build();
        rover.changeOrientation(Command.RIGHT);
        assertSame(rover.getOrientation(), Orientation.NORTH);
        verify(this.mockAppender).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals("Due to harsh weather conditions the orientation was lost. Defaulting to NORTH", loggingEvent.getMessage());
    }

    @Test
    public void moveNorthSuccessTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(5).y(5).build()).build();
        rover.moveNorth(6);
        assertEquals(6, rover.getCoordinates().getY());
    }

    @Test
    public void moveNorthFailTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(5).y(5).build()).build();
        rover.moveNorth(5);
        assertEquals(5, rover.getCoordinates().getY());
        verify(this.mockAppender).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals("The obstacle avoidance module detected that we cannot move to the North", loggingEvent.getMessage());
    }

    @Test
    public void moveSouthSuccessTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(5).y(1).build()).build();
        rover.moveSouth();
        assertEquals(0, rover.getCoordinates().getY());
    }

    @Test
    public void moveSouthFailTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(5).y(0).build()).build();
        rover.moveSouth();
        assertEquals(0, rover.getCoordinates().getY());
        verify(this.mockAppender).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals("The obstacle avoidance module detected that we cannot move to the South", loggingEvent.getMessage());
    }

    @Test
    public void moveEastSuccessTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(5).y(5).build()).build();
        rover.moveEast(6);
        assertEquals(6, rover.getCoordinates().getX());
    }

    @Test
    public void moveEastFailTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(5).y(5).build()).build();
        rover.moveEast(5);
        assertEquals(5, rover.getCoordinates().getX());
        verify(this.mockAppender).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals("The obstacle avoidance module detected that we cannot move to the East", loggingEvent.getMessage());
    }

    @Test
    public void moveWestSuccessTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(1).y(1).build()).build();
        rover.moveWest();
        assertEquals(0, rover.getCoordinates().getX());
    }

    @Test
    public void moveWestFailTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(0).y(0).build()).build();
        rover.moveWest();
        assertEquals(0, rover.getCoordinates().getX());
        verify(this.mockAppender).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        assertEquals("The obstacle avoidance module detected that we cannot move to the West", loggingEvent.getMessage());
    }

    @Test
    public void toStringTest() {
        final Rover rover = new Rover.Builder().coordinates(new Coordinates.Builder().x(0).y(0).build()).orientation(Orientation.NORTH).build();
        assertEquals("0 0 N", rover.toString());
    }
}