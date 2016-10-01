package entity;

public class ChannelItem {
    private boolean is_feed_ad;
    private String source_url;
    private String media_url;

    public String getMedia_url() {
        return media_url;
    }

    public ChannelItem(boolean is_feed_ad, String source_url, String media_url) {
        this.is_feed_ad = is_feed_ad;
        this.source_url = source_url;
        this.media_url = media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public ChannelItem() {
    }

    public ChannelItem(boolean is_feed_ad, String source_url) {
        this.is_feed_ad = is_feed_ad;
        this.source_url = source_url;

    }

    public boolean is_feed_ad() {
        return is_feed_ad;
    }

    public void setIs_feed_ad(boolean is_feed_ad) {
        this.is_feed_ad = is_feed_ad;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
