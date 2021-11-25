package com.news.reader.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageHandlerStatus {

    @Id
    @GeneratedValue
    private Long imageId;

    private String imageRemoteUrl;

    @ManyToOne
    private NewsItem newsItem;

    @Enumerated(EnumType.STRING)
    private DownloaderStatus status;

    @Transient
    private String imageLocation;

    private int retryCount;

    public void increaseRetryCount() {
        retryCount++;
    }

}



