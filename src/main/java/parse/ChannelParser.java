package parse;

import constant.Constant;
import entity.Channel;
import entity.ChannelItem;
import persist.PersistenceFactory;
import thread.ThreadPool;
import utils.Extractor;
import utils.HttpUtils;

import java.util.List;

public class ChannelParser implements Runnable {
    private String name;

    public ChannelParser(String name) {
        this.name = name;
    }

    public void run() {
        try {
            getChannel(0L);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void getChannel(Long nextPageToken) {
        String format = String.format(Constant.CHANNEL_URL, name, nextPageToken);
//        System.out.println("start get channel : " + format);
        Channel channel = HttpUtils.get(format);
        if (channel.getMessage().equals("success")) {
            List<ChannelItem> data = channel.getData();
            for (ChannelItem item : data) {
                if (!item.is_feed_ad()) {
                    String url = Constant.DOMAIN + item.getSource_url();
                    if (PersistenceFactory.getInstance().isArticleExist(Extractor.extractId(url))) {
                        continue;
                    }
                    ThreadPool.threadPoolExecutor.submit(new ArticleParser(url, true));
                }
                if (item.getMedia_url() != null && item.getMedia_url().contains(Constant.GROUP_PREFIX)) {
                    if (PersistenceFactory.getInstance().isChannelExist(item.getMedia_url())) {
                        continue;
                    }
                    PersistenceFactory.getInstance().saveChannel(item.getMedia_url());
                    ThreadPool.threadPoolExecutor.submit(new ArticleParser(item.getMedia_url(), false));
                }
            }

        }

        if (channel.isHas_more() && channel.getNext() != null && channel.getNext().getMax_behot_time() != null) {
            getChannel(channel.getNext().getMax_behot_time());
        } else {
            System.out.println("end get channel : " + name);
        }
    }
}
