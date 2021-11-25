package com.news.reader.repository;

import com.news.reader.model.ImageItem;
import com.news.reader.model.NewsItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageItemRepository extends CrudRepository<ImageItem, Long> {

    List<ImageItem> findAllByNewsItem(NewsItem newsItemId);

}
