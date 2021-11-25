package com.news.reader.repository;

import com.news.reader.model.NewsItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsItemRepository extends PagingAndSortingRepository<NewsItem, Long> {

    List<NewsItem> findByTitleContainingIgnoreCase(String title);

    List<NewsItem> findAllByLink(String status);

}
