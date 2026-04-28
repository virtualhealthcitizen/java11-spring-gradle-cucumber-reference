package org.squidmin.cucumber.skeleton.dto.bigquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BigQueryRestServiceResponse {

    private String queryId;
    private Map<String, Object> jobCreationReason;
    private String kind;
    private BigQuerySchema schema;
    private BigQueryJobReference jobReference;
    private String totalRows;
    private List<BigQueryRow> rows;
    private String totalBytesProcessed;
    private String totalBytesBilled;
    private String totalSlotMs;
    private String creationTime;
    private String startTime;
    private String endTime;
    private Map<String, Object> dmlStats;
    private String numDmlAffectedRows;
    private Map<String, Object> sessionInfo;
    private String pageToken;
    private String location;
    private boolean jobComplete;
    private boolean cacheHit;
    private List<Map<String, Object>> errors;

}
