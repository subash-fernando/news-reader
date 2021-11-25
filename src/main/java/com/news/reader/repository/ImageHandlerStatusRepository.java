package com.news.reader.repository;

import com.news.reader.model.ImageHandlerStatus;
import com.news.reader.model.DownloaderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageHandlerStatusRepository extends PagingAndSortingRepository<ImageHandlerStatus, Long> {

    Page<ImageHandlerStatus> findAllByStatus(DownloaderStatus status, Pageable pageable);

}
