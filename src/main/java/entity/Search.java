package entity;

import java.util.List;

public class Search {
    public Integer getHash_more() {
        return hash_more;
    }

    public void setHash_more(Integer hash_more) {
        this.hash_more = hash_more;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<SearchItem> getData() {
        return data;
    }

    public void setData(List<SearchItem> data) {
        this.data = data;
    }

    private Integer hash_more;
    private Integer offset;
    private List<SearchItem> data;

}
