import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JRTTCrawler {
    public static List<String> ids = new ArrayList<String>();

    public static void main(String[] args) {
        Parser.parse("http://www.toutiao.com/a6333126455618715906/", true);
    }
}

class Parser {
    public static void parse(String url, boolean isArticle) {
        Document doc;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0")
                    .ignoreContentType(true)
                    .timeout(30000)
                    .get();
        } catch (Exception e) {
            return;
        }

        if (isArticle) {
            try {
                String content;
                String title;
                String time;
                content = doc.getElementsByClass("article-content").text();
                title = doc.getElementsByClass("article-title").get(0).text();
                time = doc.getElementsByClass("articleInfo").get(0).getElementsByClass("time").text();
                String regex = "[http://toutiao.com/group/]([0-9]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(url);
                Article article = new Article(title, time, content, url);
                while (matcher.find()) {
                    String group = matcher.group(1);
                    article.setId(group);
                }

                if (JRTTCrawler.ids.contains(article.getId())) {
                    return;
                }
                JRTTCrawler.ids.add(article.getId());
                Persister.persist(article);
            } catch (Exception e) {
                return;
            }
        }

        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            if (linkHref.contains("http://toutiao.com/group")) {
                parse(linkHref, true);
            } else if (linkHref.contains("http://toutiao.com/m")) {
                parse(linkHref, false);
            }
        }
    }
}

class Article {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Article(String title, String date, String content, String url) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.url = url;
    }

    private String title;
    private String date;
    private String content;
    private String url;
    private String id;
}

class Persister {
    public static void persist(Article article) {
        String date = article.getDate();
        try {
            Date parse = new SimpleDateFormat("yy-MM-dd HH:mm").parse(date);
            String simpleDate = new SimpleDateFormat("yyMMdd").format(parse);

            String dir = System.getProperty("user.dir") + File.separator + "data";
            makeDirs(dir);
            File file = new File(dir + File.separator + simpleDate);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            String str = new Gson().toJson(article);
            bufferWritter.write(str);
            bufferWritter.write("\n");
            bufferWritter.close();
        } catch (ParseException e) {
            return;
        } catch (IOException e) {
            return;
        }


    }

    public static boolean makeDirs(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File folder = new File(filePath);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
}
