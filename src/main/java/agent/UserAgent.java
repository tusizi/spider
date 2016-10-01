package agent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAgent {
    private static List<String> userAgents = new ArrayList<String>();

    static {
        getUserAgent();
    }

    private static void getUserAgent() {
        try {
            System.out.println("start generate user agent");
            Document document = Jsoup.connect("http://www.httpuseragent.org/list/Browser-c1.htm")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0.1) Gecko/20100101 Firefox/4.0.1")
                    .timeout(60000)
                    .get();
            Elements links = document.getElementsByTag("a");
            for (Element element : links) {
                try {
                    if (element.attr("title") != null && element.attr("title").startsWith("Http User-agent")) {
                        String href = element.attr("href");
                        Document agent = Jsoup.connect(href)
                                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0.1) Gecko/20100101 Firefox/4.0.1")
                                .timeout(20000)
                                .get();
                        Elements agentCells = agent.getElementsByTag("td");
                        for (Element agentCell : agentCells) {
                            if (agentCell.attr("title") != null && agentCell.attr("title").startsWith("The details")) {
                                userAgents.add(agentCell.text());
                                System.out.print(".");
                                //                            System.out.println(agentCell.text());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            System.out.println("end generate user agent");
        } catch (IOException e) {
            e.printStackTrace();
            getUserAgent();
        }
    }

    public static String getRandomUserAgent() {
        try {
            return userAgents.get(new Random().nextInt(userAgents.size()));
        } catch (Exception e) {
            return userAgents.get(0);
        }
    }
}
