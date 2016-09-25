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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JRTTCrawler {
    public static Set<String> ids = new HashSet<String>();
    public static Set<String> channels = new HashSet<String>();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());


    public static void main(String[] args) {
        new JRTTCrawler().start();
    }

    public void start() {
        threadPoolExecutor.submit(new Parser("http://www.toutiao.com/a6333126455618715906/", true));
    }

    class Parser implements Runnable {
        private String url;
        private boolean isArticle;

        public Parser(String url, boolean isArticle) {
            this.url = url;
            this.isArticle = isArticle;
        }

        public String extractId(String url) {
            String regex = "[http://toutiao.com/group/]([0-9]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }

        public void run() {
            Document doc;
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0").ignoreContentType(true).timeout(30000).get();
            } catch (Exception e) {
                return;
            }

            if (isArticle) {
                try {
                    String title = doc.getElementsByClass("article-title").get(0).text();
                    String time = doc.getElementsByClass("articleInfo").get(0).getElementsByClass("time").text();
                    String content = doc.getElementsByClass("article-content").text();
                    String id = extractId(url);
                    if (JRTTCrawler.ids.contains(id)) {
                        return;
                    }
                    Article article = new Article(title, time, content, url, id);

                    Persistence.persist(article);
                    JRTTCrawler.ids.add(article.getId());
                    System.out.println("current article count = " + ids.size());
                } catch (Exception e) {
                    return;
                }
            }

            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.contains("http://toutiao.com/group")) {
                    String id = extractId(linkHref);
                    if (JRTTCrawler.ids.contains(id)) {
                        continue;
                    }
                    threadPoolExecutor.submit(new Parser(linkHref, true));
                } else if (linkHref.contains("http://toutiao.com/m")) {
                    if (channels.contains(linkHref)) {
                        continue;
                    }
                    channels.add(linkHref);
                    threadPoolExecutor.submit(new Parser(linkHref, false));
                }
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

    public Article(String title, String date, String content, String url, String id) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.url = url;
        this.id = id;
    }

    private String title;
    private String date;
    private String content;
    private String url;
    private String id;
}

class Persistence {
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
