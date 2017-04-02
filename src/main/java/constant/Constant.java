package constant;

import java.util.ArrayList;
import java.util.List;

public interface Constant {
    String DOMAIN = "www.domain.com";
    String ARTICLE_REGEX = "[" + DOMAIN + "/group/]([0-9]+)";
    String ARTICLE_PREFIX = DOMAIN + "/group";
    String GROUP_PREFIX = DOMAIN + "/m";
    String ARTICLE_CLASS = "article-title";
    String TIME_CLASS = "articleInfo";
    String CONTENT_CLASS = "article-content";
    String TAG_CLASS = "label-item";
    String CHANNEL_URL = DOMAIN + "/api/pc/feed/?category=%s&utm_source=toutiao&widen=1&max_behot_time=1491141008&max_behot_time_tmp=1491141008&tadrequire=false&as=A145E82E4180CDB&cp=58E120DCADFBBE1";

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
