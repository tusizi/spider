package constant;

import java.util.ArrayList;
import java.util.List;

public interface Constant {
    String DOMAIN = "www.domain.com";
    String ARTICLE_REGEX = "[" + DOMAIN + "/group/]([0-9]+)";
    String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
    String ARTICLE_PREFIX = DOMAIN + "/group";
    String GROUP_PREFIX = DOMAIN + "/m";
    String ARTICLE_CLASS = "article-title";
    String TIME_CLASS = "articleInfo";
    String CONTENT_CLASS = "article-content";
    String TAG_CLASS = "label-item";
    String CHANNEL_URL = DOMAIN + "/api/article/feed/?category=%s&utm_source=toutiao&max_behot_time=%s&as=A105975EEFD30B7&cp=57EFB3603B879E1";

    List<String> CHANNELS = new ArrayList<String>() {{
//        this.add("news_tech");
//        this.add("news_society");
//        this.add("news_finance");
//        this.add("news_sports");
        this.add("__all__");
////        this.add("software");
//        this.add("smart_home");
//        this.add("env_protection");
//        this.add("food_safety");
//        this.add("anti_corruption");
//        this.add("investment");
    }};
}
