package parse;

import constant.Constant;
import entity.Search;
import entity.SearchItem;
import persist.FileSpecificPersistence;
import persist.PersistenceFactory;
import thread.ThreadPool;
import utils.Extractor;
import utils.HttpUtils;

import java.util.List;

public class SearchParser implements Runnable {
    private String name;
    private Integer offset = 0;
    private Integer count = 20;

    public SearchParser(String name, Integer offset, Integer count) {
        this.name = name;
        this.offset = offset;
        this.count = count;
    }

    public void run() {
        try {
            doSearch(offset, name, count);
        } catch (Exception e) {
            return;
        }
    }

    private void doSearch(Integer offset, String name, Integer count) {
        String format = String.format(Constant.SEARCH_URL, offset, name, count);
        Search search = HttpUtils.getSearch(format);
        if (search != null && search.getData() != null && search.getData().size() != 0) {
            List<SearchItem> data = search.getData();
            for (SearchItem item : data) {
                if (item.getSource_url() != null) {
                    String url = Constant.DOMAIN + item.getSource_url();
                    if (PersistenceFactory.getInstance().isArticleExist(Extractor.extractId(url))) {
                        continue;
                    }
                    ArticleParser task = new ArticleParser(url, true);
                    task.setPersistence(new FileSpecificPersistence(name));
                    ThreadPool.threadPoolExecutor.submit(task);
                }
            }
            if (search.getHash_more() != null && search.getHash_more() == 1) {
                try {
                    Thread.sleep(1000L);
                    ThreadPool.threadPoolExecutor.submit(new SearchParser(name, search.getOffset(), count));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
