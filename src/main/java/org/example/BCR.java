package org.example;

import com.opencsv.CSVWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class BCR {

    static void scrapeData(WebDriver driver, File fileToSave) {
        boolean append = fileToSave.exists() && fileToSave.length() > 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave, true))) {
            if (!append) {
                String[] header = {"#", "BC Name", "Agent ID", "Mobile No", "Pincode", "Bank Name"};
                writer.writeNext(header);
            }

            WebElement table = driver.findElement(By.cssSelector("#listDiv > table"));
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            for (int i = 1; i < rows.size(); i++) { // skip header row
                try {
                    List<WebElement> cols = rows.get(i).findElements(By.tagName("td"));
                    if (cols.size() < 6) continue;

                    String serial = cols.get(0).getText().trim();
                    WebElement nameLink = cols.get(1).findElement(By.tagName("a"));
                    String bcName = nameLink.getText().replace("+", "").trim();
                    String href = nameLink.getAttribute("href");
                    String agentId = href.replaceAll(".*getBCDetailById\\('", "").replaceAll("'\\).*", "").trim();
                    String mobile = cols.get(2).getText().trim();
                    String pincode = cols.get(3).getText().trim();
                    String bankName = cols.get(4).getText().trim();

                    writer.writeNext(new String[]{serial, bcName, agentId, mobile, pincode, bankName});
                    System.out.println(serial + " -- " + bcName + " -- " + agentId + " -- " + mobile + " -- " + pincode + " -- " + bankName);
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to read row. Skipping.");
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("⚠️ Table not found. Skipping this district.");
        } catch (IOException e) {
            System.out.println("❌ Error writing CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.bcregistry.org.in/iba/home/HomeAction.do?doBCPortal=yes");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        File fileToSave = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "Bcr.csv");

        int[] stateIds = {14, 21, 28}; // Haryana, UP, Rajasthan

        for (int stateId : stateIds) {
            try {
                System.out.println("\n🌍 Selecting State ID: " + stateId);
                Select stateDropdown = new Select(wait.until(ExpectedConditions.elementToBeClickable(By.id("stateId"))));
                stateDropdown.selectByValue(String.valueOf(stateId));
                Thread.sleep(2000); // wait for district dropdown

                Select districtDropdown = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("districtId"))));
                List<WebElement> districts = districtDropdown.getOptions();

                for (int i = 1; i < districts.size(); i++) { // Skip default "-- All District --"
                    try {
                        // Refresh the dropdown reference
                        districtDropdown = new Select(driver.findElement(By.id("districtId")));
                        String districtValue = districtDropdown.getOptions().get(i).getAttribute("value");
                        String districtName = districtDropdown.getOptions().get(i).getText();
                        districtDropdown.selectByValue(districtValue);
                        Thread.sleep(1000);

                        // Click the "Go" button FIRST
                        WebElement goButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.search_btn")));
                        goButton.click();

                        // Now wait for manual CAPTCHA and verification
                        System.out.println("\n📍 Selected District: " + districtName);
                        System.out.println("🔐 Please do the following manually:");
                        System.out.println("   ✅ Enter CAPTCHA");
                        System.out.println("   ✅ Click 'Verify' button");
                        System.out.println("   ✅ Wait for table to appear");
                        System.out.print("👉 After table is visible, press [ENTER] here to scrape: ");
                        reader.readLine();

                        // If table exists, scrape it
                        if (driver.findElements(By.cssSelector("#listDiv > table")).size() > 0) {
                            scrapeData(driver, fileToSave);
                        } else {
                            System.out.println("⚠️ No data table visible. Skipping...");
                        }

                    } catch (Exception e) {
                        System.out.println("❌ District processing error: " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                System.out.println("❌ State processing error: " + e.getMessage());
            }
        }

        driver.quit();
        System.out.println("\n✅ Scraping complete.");
    }
}
