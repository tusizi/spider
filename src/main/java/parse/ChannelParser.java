package parse;

import cache.Cache;
import constant.Constant;
import entity.Channel;
import entity.ChannelItem;
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
                    if (Cache.ids.contains(Extractor.extractId(url))) {
                        continue;
                    }
                    ThreadPool.threadPoolExecutor.submit(new ArticleParser(url, true));
                }
                if (item.getMedia_url() != null && item.getMedia_url().contains(Constant.GROUP_PREFIX)) {
                    if (Cache.channels.contains(item.getMedia_url())) {
                        continue;
                    }
                    Cache.channels.add(item.getMedia_url());
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
