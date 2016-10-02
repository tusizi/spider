package parse;

import agent.UserAgent;
import constant.Constant;
import entity.Article;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import persist.PersistenceFactory;
import thread.ThreadPool;
import utils.Extractor;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ArticleParser implements Runnable {
    private String url;
    private boolean isArticle;

    public ArticleParser(String url, boolean isArticle) {
        this.url = url;
        this.isArticle = isArticle;
    }

    public void run() {
        Document doc;
        try {
            String randomUserAgent = UserAgent.getRandomUserAgent();
//            System.out.println("get by " + randomUserAgent);
            doc = Jsoup.connect(url).userAgent(randomUserAgent).ignoreContentType(true).timeout(30000).get();


            if (isArticle) {
                try {
                    Elements elementsByClass = doc.getElementsByClass(Constant.ARTICLE_CLASS);
                    if (elementsByClass.size() == 0 ||
                            doc.getElementsByClass(Constant.TIME_CLASS).size() == 0
                            || doc.getElementsByClass(Constant.CONTENT_CLASS) == null) return;
                    String title = elementsByClass.get(0).text();
                    String time = doc.getElementsByClass(Constant.TIME_CLASS).get(0).getElementsByClass("time").text();
                    String content = doc.getElementsByClass(Constant.CONTENT_CLASS).text();
                    String id = Extractor.extractId(url);
                    if (PersistenceFactory.getInstance().isArticleExist(id)) {
                        return;
                    }

                    Elements tagElements = doc.getElementsByClass(Constant.TAG_CLASS);
                    List<String> tags = new ArrayList<String>();
                    for (Element element : tagElements) {
                        tags.add(element.text());
                    }

                    Article article = new Article(title, time, content, url, id, tags);

                    PersistenceFactory.getInstance().saveArticle(article);
                    System.out.print(".");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }

            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                if (linkHref.contains(Constant.ARTICLE_PREFIX)) {
                    String id = Extractor.extractId(linkHref);
                    if (PersistenceFactory.getInstance().isArticleExist(id)) {
                        continue;
                    }
                    ThreadPool.threadPoolExecutor.submit(new ArticleParser(linkHref, true));
                } else if (linkHref.contains(Constant.GROUP_PREFIX)) {
                    if (PersistenceFactory.getInstance().isChannelExist(linkHref)) {
                        continue;
                    }
                    PersistenceFactory.getInstance().saveChannel(linkHref);
                    ThreadPool.threadPoolExecutor.submit(new ArticleParser(linkHref, false));
                }
            }
        } catch (Exception e) {
            if (e instanceof HttpStatusException) {
                return;
            }
            if (e instanceof SocketTimeoutException)
                return;

            if (e instanceof java.net.ConnectException)
                return;

            if (e instanceof UnknownHostException)
                return;
            e.printStackTrace();
            return;
        }
    }

}
