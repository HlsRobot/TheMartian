package Xing.TheMartian;

import Xing.TheMartian.Services.InputService;
import Xing.TheMartian.Services.MartianService;
import Xing.TheMartian.domain.InputDto;
import Xing.TheMartian.domain.Rover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MartianRunner {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputService inputService = new InputService(br);
        final InputDto inputDto = inputService.provideInitialInput();
        final MartianService martianService = new MartianService();
        final List<Rover> roverList = martianService.startMartianApp(inputDto);
        roverList.forEach(rover -> System.out.println(rover.toString()));
    }
}
