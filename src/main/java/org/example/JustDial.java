package org.example;

import com.opencsv.CSVWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;


public class JustDial {
    static void scrapeData(WebDriver driver, File fileToSave) {
        boolean append = fileToSave.exists() && fileToSave.length() > 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave, true))) {
            if (!append) {
                String[] header = {"Name", "AgentId", "Phone", "Address"};
                writer.writeNext(header);
            }else {
                writer.writeNext(new String[]{""});
            }
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            List<WebElement> agentCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector(".card-body ul")
            ));

            for (WebElement ul : agentCards) {
                try {
                    List<WebElement> liTags = ul.findElements(By.tagName("li"));
                    if (liTags.size() < 2) continue;

                    List<WebElement> spans = liTags.get(1).findElements(By.tagName("span"));
                    if (spans.size() < 4) continue;

                    String name = spans.get(0).getText().replace("Name:", "").trim();
                    String agentId = spans.get(1).getText().replace("Agent Id:", "").trim();
                    String phone = spans.get(2).getText().replace("Phone:", "").trim();
                    String address = spans.get(3).getText().replace("Address:", "").trim();

                    writer.writeNext(new String[]{name, agentId, phone, address});
                    System.out.println(name + " -- " + agentId + " -- " + phone + " -- " + address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("webdriver.chrome.driver", "C:/windows/chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get("https://www.justdial.com/Gurgaon/Paying-Guest-Accommodations-For-Women/nct-11273561");


        String userHome = System.getProperty("user.home");
        String downloadPath = userHome + File.separator + "Downloads" + File.separator + "EmployeeUP.csv";
        File fileToSave = new File(downloadPath);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        System.out.println("⏳ Please manually select a state...");

        // Wait for you to manually select a state and cities to appear
        Thread.sleep(8000); // wait 8 seconds (you can increase if needed)

        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("category1")));
        Select select = new Select(dropdown);
        List<WebElement> options = select.getOptions();

        System.out.println("📋 Total Cities Found: " + (options.size() - 1)); // exclude "Select City"

        for (int i = 1; i < options.size(); i++) { // Start from 1 to skip the default "Select City"
            try {
                dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("category1")));
                select = new Select(dropdown);

                WebElement option = select.getOptions().get(i);
                String cityName = option.getText().trim();

                System.out.println("➡️ Selecting city: " + cityName);
                select.selectByVisibleText(cityName);

                // Wait for city-specific data to load
                Thread.sleep(4000); // Adjust if necessary

                scrapeData(driver, fileToSave);
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                i--; // retry same index
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("✅ Scraping completed for all cities.");
    }
}



