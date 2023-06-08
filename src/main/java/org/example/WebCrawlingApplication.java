package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.jsoup.nodes.Element;

public class WebCrawlingApplication {
    private static final int MAX_DEPTH = 2;

    private final List<List<String>> titlesList;
    private final HashSet<String> urlLinksSet;

    public WebCrawlingApplication() {
        titlesList = new ArrayList<>();
        urlLinksSet = new HashSet<>();
    }

    public void writeDataToTheFile(String fileName) {
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(fileName);

            for (List<String> title : titlesList) {

                try {
                    String article = title.get(0) + "\n"+ "=>"+ title.get(1) + "\n";

                    System.out.println(article);
                    fileWriter.write(article + "\n");

                } catch (IOException err) {
                    System.out.println(err.getMessage());
                }
            }
            fileWriter.close();
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
    }

    public void getPageLinks(String URL, int depthDegree) {
        if (urlLinksSet.size() != 100 && !urlLinksSet.contains(URL) && (depthDegree < MAX_DEPTH) && (URL.startsWith("https://www.skysports.com/"))) {
            System.out.println("crawling at " + depthDegree + ". degree now" + "\n" + " =>" + URL );

            try {
                urlLinksSet.add(URL);

                Document doc = Jsoup.connect(URL).get();

                Elements linksOnTheSourcePage = doc.select("a[href]");

                depthDegree++;

                for (Element element : linksOnTheSourcePage) {
                    String absoluteURL = element.attr("abs:href");
                    if (absoluteURL.startsWith("https://www.skysports.com/") && !urlLinksSet.contains(absoluteURL)) {
                        getPageLinks(absoluteURL, depthDegree);
                    }
                }
            } catch (IOException err) {
                System.out.println(URL + err.getMessage());
            }
        }
    }

    public void getArticles() {
        Iterator<String> iterator = urlLinksSet.iterator();
        HashSet<String> visitedArticleLinks = new HashSet<>();

        while (iterator.hasNext()) {
            Document doc;

            try {
                doc = Jsoup.connect(iterator.next()).get();
                Elements availableArticleLinks = doc.select("a[href]");

                for (Element ele : availableArticleLinks) {
                    if (ele.text().contains("golf")) {
                        String articleLink = ele.attr("abs:href");

                        if (!visitedArticleLinks.contains(articleLink)) {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(ele.text());
                            temp.add(articleLink);
                            titlesList.add(temp);
                            visitedArticleLinks.add(articleLink);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }


}
