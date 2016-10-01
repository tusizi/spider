package entity;

import java.util.List;

public class Channel {
    public Channel() {
    }

    public Channel(boolean has_more, String message, List<ChannelItem> data) {
        this.has_more = has_more;
        this.message = message;
        this.data = data;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChannelItem> getData() {
        return data;
    }

    public void setData(List<ChannelItem> data) {
        this.data = data;
    }

    private boolean has_more;
    private String message;
    private List<ChannelItem> data;
    private Next next;

    public Next getNext() {
        return next;
    }

    public void setNext(Next next) {
        this.next = next;
    }
}
