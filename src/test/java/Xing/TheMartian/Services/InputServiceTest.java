package Xing.TheMartian.services;

import Xing.TheMartian.domain.InputDto;
import Xing.TheMartian.enums.Orientation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class InputServiceTest {

    private BufferedReader bufferedReader;

    private InputService inputService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.setOut(new PrintStream(this.outContent));
        System.setErr(new PrintStream(this.errContent));
        this.bufferedReader = Mockito.mock(BufferedReader.class);
        this.inputService = new InputService(this.bufferedReader);
    }


    @Test
    public void provideInitialInputSuccessTest() throws IOException {
        Mockito.when(this.bufferedReader.readLine()).thenReturn("5 6", "3 3 N", "MLMLMLML", "N");
        final InputDto inputDto = this.inputService.provideInitialInput();
        assertEquals(5, inputDto.getLimitCoordinates().getX());
        assertEquals(6, inputDto.getLimitCoordinates().getY());
        assertEquals(1, inputDto.getRoverCommandList().size());
        assertEquals(Orientation.NORTH, inputDto.getRoverCommandList().get(0).getRover().getOrientation());
        assertEquals("MLMLMLML", inputDto.getRoverCommandList().get(0).getCommandLine());
    }

    @Test
    public void provideInitialInputSuccessMultipleTest() throws IOException {
        Mockito.when(this.bufferedReader.readLine()).thenReturn("5 6", "3 3 N", "MLMLMLML", "Y", "1 2 E", "RMLRML", "");
        final InputDto inputDto = this.inputService.provideInitialInput();
        assertEquals(2, inputDto.getRoverCommandList().size());
    }

    @Test
    public void provideInitialInputWithWrongLimitCoordinatesTest() throws IOException {
        Mockito.when(this.bufferedReader.readLine()).thenReturn(" ", "0 1", "a d", "5 5", "3 3 N", "MLMLMLML", "N");
        this.inputService.provideInitialInput();
        assertTrue(this.errContent.toString().contains("Please provide exactly 2 coordinates"));
        assertTrue(this.errContent.toString().contains("Please provide a value larger than 0"));
        assertTrue(this.errContent.toString().contains("Please provide Integer values larger than 0 whitespace separated"));
    }

    @Test
    public void provideInitialInputWithWrongRoverTest() throws IOException {
        Mockito.when(this.bufferedReader.readLine()).thenReturn("5 5", "5 5", "6 6 N", "3 3 A", "a f E", "3 3 N", "MLMLMLML", "N");
        this.inputService.provideInitialInput();
        assertTrue(this.errContent.toString().contains("Please provide the correct amount of parameters"));
        assertTrue(this.errContent.toString().contains("please provide coordinates inside the limits of the plateau"));
        assertTrue(this.errContent.toString().contains("Please provide a correct orientation"));
        assertTrue(this.errContent.toString().contains("Please provide integer values larger than 0 whitespace separated followed by the orientation"));
    }

    @Test
    public void provideInitialInputWithWrongCommandsTest() throws IOException {
        Mockito.when(this.bufferedReader.readLine()).thenReturn("5 5", "3 3 N", "abcmrlabcmrlabcmrl", "N");
        final InputDto inputDto = this.inputService.provideInitialInput();
        assertTrue(this.errContent.toString().contains("Character a is not a valid command character thus removed from the command line"));
        assertTrue(this.errContent.toString().contains("Character b is not a valid command character thus removed from the command line"));
        assertTrue(this.errContent.toString().contains("Character c is not a valid command character thus removed from the command line"));
        assertEquals("MRLMRLMRL", inputDto.getRoverCommandList().get(0).getCommandLine());
    }


}