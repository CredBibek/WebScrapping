//package org.example;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.List;
//
//public class A {
//    public static void main(String[] args) {
//        System.setProperty("webdriver.chrome.driver", "C:/windows/chromedriver.exe");
//
//        WebDriver driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Use Duration for implicit wait
//        driver.get("https://www.flipkart.com/search?q=macbook");
//
//        // Close the login pop-up if it appears
//        try {
//            WebElement popupCloseButton = driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z"));
//            popupCloseButton.click();
//        } catch (Exception e) {
//            System.out.println("No pop-up appeared or could not close it.");
//        }
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("MacBooks");
//
//            Row headerRow = sheet.createRow(0);
//            headerRow.createCell(0).setCellValue("Title");
//            headerRow.createCell(1).setCellValue("Price");
//
//            // Use Duration for explicit wait
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".cPHDOP")));
//
//            int rowNum = 1;
//            for (WebElement product : products) {
//                try {
//                    String title = product.findElement(By.cssSelector(".KzDlHZ")).getText();
//                    String price = product.findElement(By.cssSelector("Nx9bqj _4b5DiR")).getText();
//
//                    System.out.println(title + " -- " + price);
//
//                    Row row = sheet.createRow(rowNum++);
//                    row.createCell(0).setCellValue(title);
//                    row.createCell(1).setCellValue(price);
//                } catch (Exception ignored) {
//                    ignored.printStackTrace();
//                }
//            }
//
//            try (FileOutputStream fileOut = new FileOutputStream("MacBooks.xlsx")) {
//                workbook.write(fileOut);
//            }
//
//            System.out.println("Data successfully written to MacBooks.xlsx");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit();
//        }
//    }
//}
//
