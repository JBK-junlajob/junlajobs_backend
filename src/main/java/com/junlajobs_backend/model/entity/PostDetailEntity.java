package com.junlajobs_backend.model.entity;
import com.google.cloud.Timestamp;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PostDetailEntity {
    private String creator;
    private String explanation;
    private String job_title;
    private String price_end;
    private String price_start;
    private Date release_date;
    private String latitude;
    private String longitude;
    private String lastUpdate;
    private String type;
    private int like;
}
