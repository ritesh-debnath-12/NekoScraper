package me.neko.UniScraper.utils;

import me.tongfei.progressbar.ProgressBar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class Runner {
    private static final Logger logger = LogManager.getLogger(Runner.class);

    public static void run() {
        WebDriver driver = null;
        try {
            Initializer init = Initializer.WebDriverInit();
            driver = init.driver;
            Map<String, String> env = init.env;
            String roll_prefix = env.get("ROLL_PREFIX").endsWith("-") ? env.get("ROLL_PREFIX") : env.get("ROLL_PREFIX") + "-";
            String semester = env.get("SEMESTER");
            String year = env.get("YEAR");
            int i = Integer.parseInt(env.get("MIN_VALUE"));
            int k = Integer.parseInt(env.get("MAX_VALUE"));
            boolean isSemesterEven = ((Integer.parseInt(semester) % 2) == 0);
            int total = k - i + 1;

            if (i > k) {
                logger.fatal("MIN_VALUE is greater than MAX_VALUE. Please check your .env");
                return;
            }

            logger.info("Starting Scraper....");

            ScrapingUtilites utils = new ScrapingUtilites(semester);

            try (ProgressBar pb = new ProgressBar("Fetching Data", total)) {
                while (i <= k) {
                    String roll = utils.convertIntegerToRollNumber(roll_prefix, i);
                    logger.info("Now processing roll: " + roll);
                    utils.sendBasicInformation(driver, roll, semester, year);
                    boolean isStudentValid = utils.grabInformationFromReport(driver, roll, isSemesterEven);
                    if (!isStudentValid) {
                        i++;
                        pb.step();
                        utils.goBackToPreviousPage(driver);
                    } else {
                        i++;
                        pb.step();
                        utils.downloadReport(driver);
                        utils.goBackToPreviousPage(driver);
                    }
                }
            }
            logger.info("Completed processing!");
        } catch (Exception e) {
            logger.fatal("Oh shit, something died...not good. INFO: {}", e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
                logger.info("Driver successfully closed!");
            }
        }
    }
}
