package org.example;

public class Main {
    public static void main(String[] args) {
        WebCrawlingApplication webCrawlingApplication = new WebCrawlingApplication();
        webCrawlingApplication.getPageLinks("https://www.skysports.com/", 0);
        webCrawlingApplication.getArticles();
        webCrawlingApplication.writeDataToTheFile("dataFile.txt");
    }
}