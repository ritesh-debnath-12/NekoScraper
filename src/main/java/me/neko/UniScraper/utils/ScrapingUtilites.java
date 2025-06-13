package me.neko.UniScraper.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ScrapingUtilites {

    private final Logger logger;
    private final String STUDENT_DATA_PATH;
    private final String STUDENT_MISSING_PATH;

    public ScrapingUtilites(String semester) throws IOException {
        this.logger = LogManager.getLogger(ScrapingUtilites.class);
        this.STUDENT_DATA_PATH = "data/" + "semester"+semester+ "/student_data.csv";
        this.STUDENT_MISSING_PATH = "data/" + "semester"+semester+ "/student_missing.csv";

        this.logger.info("ScrapingUtilities Instance created successfully!");

        BufferedWriter csv_writer = new BufferedWriter(new FileWriter(this.STUDENT_DATA_PATH, false));
        csv_writer.write("Name,Roll Number,Registration Number,GPA\n");
        csv_writer.close();

        csv_writer = new BufferedWriter(new FileWriter(this.STUDENT_MISSING_PATH, false));
        csv_writer.write("Missing Roll\n");
        csv_writer.close();

        this.logger.info("Populated initial headings for csv file(s)");
    }

    private void writeToCSV(String roll) throws IOException {
        BufferedWriter csv_writer = new BufferedWriter(new FileWriter(this.STUDENT_MISSING_PATH, true));
        csv_writer.write(roll + "\n");
        csv_writer.close();
        this.logger.info("Wrote data for " + roll + " in " + this.STUDENT_MISSING_PATH);

    }

    //this one is for odd semester
    private void writeToCSV(String name, String roll, String regno, String gpa) throws IOException {
        BufferedWriter csv_writer = new BufferedWriter(new FileWriter(this.STUDENT_DATA_PATH, true));
        StringBuilder query = new StringBuilder();
        query
                .append(name)
                .append(",")
                .append(roll)
                .append(",")
                .append(regno)
                .append(",")
                .append(gpa)
                .append("\n");
        csv_writer.write(query.toString());
        csv_writer.close();
        this.logger.info("Wrote data for " + roll + " in " + this.STUDENT_DATA_PATH);
    }

    //this one is for even semester
    private void writeToCSV(String name, String roll, String regno, String gpa_1, String gpa_2, String ygpa) throws IOException {
        BufferedWriter csv_writer = new BufferedWriter(new FileWriter(this.STUDENT_DATA_PATH, true));
        StringBuilder query = new StringBuilder();
        query
                .append(name)
                .append(",")
                .append(roll)
                .append(",")
                .append(regno)
                .append(",")
                .append(gpa_1)
                .append(",")
                .append(gpa_2)
                .append(",")
                .append(ygpa)
                .append("\n");
        csv_writer.write(query.toString());
        csv_writer.close();
        this.logger.info("Wrote data for " + roll + " in " + this.STUDENT_DATA_PATH);
    }

    public String convertIntegerToRollNumber(String roll_prefix, int n) {
        if (n >= 0 && n <= 9) return (roll_prefix + "00" + n);
        else if (n >= 10 && n <= 99) return (roll_prefix + "0" + n);
        else return (roll_prefix + n);
    }


    public void sendBasicInformation(WebDriver driver, String rollNumber, String semester, String year) {
        this.logger.info("Sending basic information...");
        WebElement roll_number_input = driver.findElement(By.name("roll"));
        WebElement semester_dropdown = driver.findElement(By.name("sem"));
        WebElement year_dropdown = driver.findElement(By.name("yr"));
        WebElement submit_btn = driver.findElement(By.id("Button1"));

        roll_number_input.clear();
        roll_number_input.sendKeys(rollNumber);

        Select semester_select = new Select(semester_dropdown);
        semester_select.selectByValue(semester);

        Select year_select = new Select(year_dropdown);
        year_select.selectByValue(year);

        submit_btn.click();
    }

    public boolean grabInformationFromReport(WebDriver driver, String roll, boolean isSemesterEven) throws IOException {
        try {
            if (isSemesterEven) {
                WebElement raw_name = driver.findElement(By.id("lblname"));
                WebElement raw_regno = driver.findElement(By.id("lblrg"));
                WebElement raw_gpa_1 = driver.findElement(By.id("lblbottom1"));
                WebElement raw_gpa_2 = driver.findElement(By.id("lblbottom2"));
                WebElement raw_ygpa = driver.findElement(By.id("lblbottom3"));

                this.logger.info("Found " + roll + "'s data!");

                String name = raw_name.getText().trim();
                String regno = raw_regno.getText().trim();
                String gpa_1 = raw_gpa_1.getText().trim();
                String gpa_2 = raw_gpa_2.getText().trim();
                String ygpa = raw_ygpa.getText().trim();

                writeToCSV(name, roll, regno, gpa_1, gpa_2, ygpa);
            } else {
                WebElement raw_regno = driver.findElement(By.id("lblrg"));
                WebElement raw_name = driver.findElement(By.id("lblname"));
                WebElement raw_gpa = driver.findElement(By.id("lblbottom1"));

                this.logger.info("Found " + roll + "'s data!");

                String name = raw_name.getText().trim();
                String regno = raw_regno.getText().trim();
                String gpa = raw_gpa.getText().trim();

                writeToCSV(name, roll, regno, gpa);
            }

            return true;
        } catch (Exception e) {
            this.logger.warn("Did not get " + roll + "'s data");
            writeToCSV(roll);
            return false;
        }
    }

    public void downloadReport(WebDriver driver, boolean isChrome) throws InterruptedException {
        this.logger.info("Attempting to download pdf");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement download_btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("create_pdf")));
        download_btn.click();

        if(!isChrome){
            wait.until(d -> driver.getWindowHandles().size() > 1);
            this.logger.info("Succeeded in downloading pdf!");

            List<Object> tabs = Arrays.asList(driver.getWindowHandles().toArray());
            driver.switchTo().window(tabs.get(1).toString());
            driver.close();
            driver.switchTo().window(tabs.get(0).toString());
        }else{
            Thread.sleep(Duration.ofSeconds(3));
            this.logger.info("Succeeded in downloading pdf!"); // Chrome does silent stuff...
        }
    }

    public void goBackToPreviousPage(WebDriver driver) {
        driver.navigate().back();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("yr")));
    }
}
