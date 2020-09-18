package suncrafterina.service.dto;

import java.util.List;

public class SaleGraphDto {

    private List<String> labels;

    private List<Long> data;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Long> getData() {
        return data;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }
}
