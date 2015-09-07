package ru.jleague13.repository;

import ru.jleague13.entity.NewsItem;

import java.util.List;

/**
 * @author ashevenkov 06.09.15 21:47.
 */
public interface NewsRepository {

    List<NewsItem> lastNews(int count);

    NewsItem getNewsItem(int id);

    List<NewsItem> lastNewsBefore(int id, int count);
}
