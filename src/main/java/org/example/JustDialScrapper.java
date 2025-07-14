package org.example;

import com.opencsv.CSVWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class JustDialScrapper {

    public static void main(String[] args) throws InterruptedException {
        // Setup ChromeDriver with options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.justdial.com/Delhi/CA/nct-10066128?trkid=91565-delhi&term=");

        Set<String> scraped = new HashSet<>();
        List<String[]> csvData = new ArrayList<>();
        String userHome = System.getProperty("user.home");
        String downloadPath = userHome + File.separator + "Downloads" + File.separator + "CyberCafesG.csv";
        File fileToSave = new File(downloadPath);
        boolean append = fileToSave.exists() && fileToSave.length() > 0;

        if (!append) {
            csvData.add(new String[]{"Name", "Phone"});
        }

        int scrollCount = 0;

        while (scrollCount < 20) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(3000);

            List<WebElement> listings = driver.findElements(By.cssSelector("div.resultbox"));

            for (WebElement listing : listings) {
                try {
                    String name = listing.findElement(By.cssSelector("h3.resultbox_title_anchor")).getText().trim();
                    WebElement phoneElement = listing.findElement(By.cssSelector("span.callcontent"));

                    String phone = "";
                    List<WebElement> telLinks = phoneElement.findElements(By.cssSelector("a[href^='tel:']"));
                    if (!telLinks.isEmpty()) {
                        phone = telLinks.get(0).getText().trim();
                    }

                    if (!phone.isEmpty()) {
                        String uniqueKey = name + phone;
                        if (!scraped.contains(uniqueKey)) {
                            scraped.add(uniqueKey);
                            csvData.add(new String[]{name, phone});
                            System.out.println("✅ Name: " + name + " | Phone: " + phone);
                        }
                    }

                } catch (Exception e) {
                    // skip bad entries
                }
            }

            scrollCount++;
        }

        driver.quit();

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave, true))) {
            writer.writeAll(csvData);
            System.out.println("📁 Data saved to: " + downloadPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
