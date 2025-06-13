package me.neko;

import me.neko.UniScraper.utils.Runner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        //trust me this would be smaller if I just knew how decorators worked in Java
        long start = System.currentTimeMillis();
        Runner.run();
        long end = (System.currentTimeMillis() - start) / 1000;
        logger.info("Time elapsed(in seconds): " + end);
        System.out.println("Press [ENTER] to exit tool!");
        new Scanner(System.in).nextLine();
    }
}