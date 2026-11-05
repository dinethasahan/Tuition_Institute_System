package com.tuition.institute.report;

import java.util.Map;

public class DashboardDTO {
    private Map<String, Object> metrics;

    public DashboardDTO() {}

    public DashboardDTO(Map<String, Object> metrics) {
        this.metrics = metrics;
    }

    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
}
