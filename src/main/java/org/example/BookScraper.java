//package org.example;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class BookScraper {
//    public static void main(String[] args) {
//        String url = "https://books.toscrape.com/";
//        String excelFilePath = "BooksData.xlsx";
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Books");
//
//            // Create header row
//            Row headerRow = sheet.createRow(0);
//            headerRow.createCell(0).setCellValue("Title");
//            headerRow.createCell(1).setCellValue("Price");
//
//            // Scrape data
//            Document document = Jsoup.connect(url).get();
//            Elements books = document.select(".product_pod");
//
//            int rowNum = 1;
//            for (Element bk : books) {
//                String title = bk.select("h3 > a").text();
//                String price = bk.select(".price_color").text();
//
//                // Write data to Excel
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(title);
//                row.createCell(1).setCellValue(price);
//            }
//
//            // Save the Excel file
//            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                workbook.write(fileOut);
//            }
//
//            System.out.println("Data successfully written to " + excelFilePath);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//    }
//}
