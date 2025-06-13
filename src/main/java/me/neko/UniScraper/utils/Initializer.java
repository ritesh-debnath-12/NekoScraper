package me.neko.UniScraper.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Initializer {
    public WebDriver driver;
    public Map<String, String> env;

    public Initializer(WebDriver driver, Map<String, String> env) {
        this.driver = driver;
        this.env = env;
    }

    private static void createInitialFolderHierarchy(Logger logger) {
        File path = new File("logs");
        if (!(path.exists())) {
            new File("logs").mkdirs();
            logger.info("Folder 'logs' is not present, created..");
        } else {
            logger.info("Folder 'logs' present, skipping");

            path = new File("data");
        }
        if (!(path.exists())) {
            new File("data").mkdirs();
            logger.info("Folder 'data' is not present, created..");
        } else {
            logger.info("Folder 'data' present, skipping");
        }
    }

    private static void createFinalFolderHierarchy(Logger logger, String semester) {
        String pathname = "data/semester" + semester;
        File path = new File(pathname);
        if (!(path.exists())) {
            new File(pathname).mkdirs();
            logger.info("Folder " + pathname + " is not present, created...");
        } else {
            logger.info("Folder " + pathname + " is present, skipping");
        }
    }

    public static Initializer WebDriverInit() throws IOException {
        Logger logger = LogManager.getLogger(Initializer.class);
        createInitialFolderHierarchy(logger);
        logger.info("Beginning Initialization of Project");
        logger.info("Attempting to parse .env file");
            Map<String, String> env = null;
        try {
            env = EnvLoader.loadEnv(".env");
            logger.info(".env file has been parsed successfully!");
            logger.info("Creating other env dependent folders");
            createFinalFolderHierarchy(logger, env.get("SEMESTER"));
        } catch (Exception e) {
            logger.fatal(".env file missing!!!! Please follow the instructions and/or send the log file to repo owner :D");
        }

        WebDriver driver;

        System.setProperty("webdriver.gecko.driver", env.get("GECKO_DRIVER_PATH"));
        System.setProperty("webdriver.chrome.driver", env.get("CHROME_DRIVER_PATH"));
        try {
            FirefoxOptions firefoxOptions = new FirefoxOptions();

            firefoxOptions.setBinary(env.get("BROWSER_BINARY_PATH"));
            driver = new FirefoxDriver(firefoxOptions);
            logger.info("Successfully initialized Marionette and Gecko Driver");
        } catch (Exception e) {
            logger.warn("Gecko initialization failed with error: " + e.getMessage());
            logger.error("Alert! Browser not applicable with Gecko Driver, falling back to Chrome!");
            ChromeOptions chromeOptions = new ChromeOptions();

            chromeOptions.setBinary(env.get("BROWSER_BINARY_PATH"));
            driver = new ChromeDriver(chromeOptions);
            logger.info("Successfully initialized Chrome ");
        }
        driver.get(env.get("PAYLOAD_URL"));
        logger.info("Initialization of Web Driver completed successfully!");
        return new Initializer(driver, env);
    }
}