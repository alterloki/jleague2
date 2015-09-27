package ru.jleague13.repository;

import ru.jleague13.entity.NewsArticle;
import ru.jleague13.entity.NewsSnippet;

import java.util.List;

/**
 * @author ashevenkov 06.09.15 21:47.
 */
public interface NewsDao {

    List<NewsSnippet> lastNewsSnippets(int count);

    NewsArticle getNewsArticle(int id);

    List<NewsSnippet> lastNewsBefore(int fromCount, int count);
}
