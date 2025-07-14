package org.example;

import com.opencsv.CSVWriter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class GrotalScraper {

    static WebDriver driver;

    public static void scrapeFromUrl(String url, CSVWriter writer) {
        try {
            int pageNum = 1;
            boolean morePages = true;

            while (morePages) {
                // Replace page number in URL dynamically like ...P1A0, P2A0 etc.
                String pageUrl = url.replaceAll("P\\d+A0", "P" + pageNum + "A0");
                driver.get(pageUrl);
                Thread.sleep(3000);

                List<WebElement> results = driver.findElements(By.cssSelector(".result-row"));
                if (results.isEmpty()) {
                    System.out.println("⚠️ No results found on page " + pageNum);
                    break;
                }

                for (WebElement result : results) {
                    try {
                        // Title: div with class 'cursor', has 'title' attr, and onclick contains Navigate()
                        WebElement titleElem = result.findElement(By.cssSelector(".cursor[title][onclick*='Navigate']"));
                        String title = titleElem.getAttribute("title").trim();

                        // Address inside .address div
                        String address = "N/A";
                        try {
                            WebElement addrElem = result.findElement(By.cssSelector(".address"));
                            address = addrElem.getText().trim();
                        } catch (NoSuchElementException ignored) {}

                        // Phone(s): from .ph-no div, join multiple phone numbers separated by comma
                        String phone = "N/A";
                        try {
                            WebElement phoneElem = result.findElement(By.cssSelector(".ph-no"));
                            String rawPhones = phoneElem.getText().trim().replace("\u00A0", " "); // Replace non-breaking space
                            phone = rawPhones.replaceAll("\\s{2,}", ", ");
                        } catch (NoSuchElementException ignored) {}

                        System.out.println("✅ Title: " + title);
                        System.out.println("   Address: " + address);
                        System.out.println("   Phone(s): " + phone);

                        writer.writeNext(new String[]{title, address, phone});
                    } catch (Exception e) {
                        System.out.println("⚠️ Skipping a malformed listing: " + e.getMessage());
                    }
                }

                // Pagination check: look for next page number in pagination ul
                List<WebElement> pages = driver.findElements(By.cssSelector("ul.pagination li a"));
                boolean hasNext = false;
                for (WebElement pageLink : pages) {
                    String text = pageLink.getText().trim();
                    try {
                        int num = Integer.parseInt(text);
                        if (num == pageNum + 1) {
                            hasNext = true;
                            break;
                        }
                    } catch (NumberFormatException ignored) {}
                }

                if (hasNext) {
                    pageNum++;
                } else {
                    morePages = false;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error scraping URL " + url + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Set your actual user downloads path here:
        String downloadsPath = System.getProperty("user.home") + "/Downloads/Hulu_lulu_lu3.csv";

        // List your URLs here:
        List<String> urls = List.of(

                "https://www.grotal.com/Agra/kirana-store-C85A0P1A0/",
                "https://www.grotal.com/Agra/csc-center-C85A0P1A0/"
        );

        try (CSVWriter writer = new CSVWriter(new FileWriter(downloadsPath, true))) {
            writer.writeNext(new String[]{"Title", "Address", "Phone"});

            for (String url : urls) {
                System.out.println("🔍 Scraping URL: " + url);
                scrapeFromUrl(url, writer);
            }

        } catch (IOException e) {
            System.out.println("❌ CSV writing error: " + e.getMessage());
        } finally {
            driver.quit();
        }

        System.out.println("✅ Scraping complete! Saved to: " + downloadsPath);
    }
}
