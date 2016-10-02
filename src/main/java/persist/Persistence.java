package persist;

import entity.Article;

public interface Persistence {
    void saveArticle(Article article);

    int articleCount();

    boolean isChannelExist(String id);

    boolean isArticleExist(String id);

    void saveChannel(String id);
}
