package entity;

import java.util.List;

public class Search {
    public Integer getHas_more() {
        return has_more;
    }

    public void setHas_more(Integer has_more) {
        this.has_more = has_more;
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

    private Integer has_more;
    private Integer offset;
    private List<SearchItem> data;

}
