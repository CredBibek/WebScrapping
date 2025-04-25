package org.example;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class FlipkartScraper {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:/windows/chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.flipkart.com/search?q=macbook");

        String userHome = System.getProperty("user.home");
        String downloadPath = userHome + File.separator + "Downloads" + File.separator + "MacBooks.csv";
        File fileToSave = new File(downloadPath);

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))) {

            String[] header = {"Title", "Price"};
            writer.writeNext(header);

            // Use explicit wait to ensure elements are loaded
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".yKfJKb")));

            for (WebElement product : products) {
                try {
                    String title = product.findElement(By.cssSelector("div > .KzDlHZ")).getText();
                    String price = product.findElement(By.cssSelector("div > div > div > .Nx9bqj")).getText();
                    System.out.println(title + " -- " + price);
                   

                    // Write data to CSV
                    writer.writeNext(new String[]{title, price});
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }

            System.out.println("Data successfully saved to: " + downloadPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.quit();
    }
}
