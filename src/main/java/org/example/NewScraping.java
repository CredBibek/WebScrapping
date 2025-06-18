//package org.example;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.io.FileOutputStream;
//import java.time.Duration;
//import java.util.List;
//import java.util.Set;
//
//public class NewScraping {
//    static int rowNum = 1;
//
//    static void putData(WebDriver driver, Sheet sheet) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//
//        List<WebElement> products = wait
//                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".yKfJKb")));
//        System.out.println("--------" + products.size() + "-------------");
//
//        for (WebElement product : products) {
//
//            try {
//                String title = product.findElement(By.cssSelector("div > .KzDlHZ")).getText();
//                String price = product.findElement(By.cssSelector("div > div > div > .Nx9bqj")).getText();
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(title);
//                row.createCell(1).setCellValue(price);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    public static void main(String[] args) {
//        WebDriver driver = new ChromeDriver();
//        driver.get("https://www.flipkart.com/");
//        driver.manage().window().maximize();
//        WebElement searchBox = driver.findElement(By.cssSelector(".Pke_EE"));
//        searchBox.sendKeys("macbooks");
//        searchBox.submit();
//        String mainPage = driver.getWindowHandle();
//        System.out.println("Mian page: " + mainPage);
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // Creating xlel sheet
//
//            Sheet sheet = workbook.createSheet("MacLists");
//            Row headerRow = sheet.createRow(0);
//            headerRow.createCell(0).setCellValue("Title");
//            headerRow.createCell(1).setCellValue("Price");
//
//            // putData(driver, sheet);
//
//            try {
//
//                while (driver.findElement(By.xpath("//span[normalize-space()='Next']")).getText()
//                        .equalsIgnoreCase("Next")) {
//
//                    System.out.println("Top of it");
//                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//
//                    // Check and close login popup if present
//                    try {
//                        WebElement popupCloseBtn = wait
//                                .until(ExpectedConditions
//                                        .elementToBeClickable(By.cssSelector("button._2KpZ6l._2doB4z")));
//                        popupCloseBtn.click();
//                    } catch (Exception e) {
//
//                    }
//
//                    // Click pagination link using JavaScript
//                    WebElement element = wait
//                            .until(ExpectedConditions
//                                    .elementToBeClickable(By.xpath("//span[normalize-space()='Next']")));
//
//                    element.click();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try (FileOutputStream outFile = new FileOutputStream("MacBooks.xlsx")) {
//                workbook.write(outFile);
//            }
//            System.out.println("Data successfully written  MacBooks.xlsx");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        finally {
//            driver.quit();
//        }
//
//    }
//
//}
