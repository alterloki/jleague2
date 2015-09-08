package ru.jleague13.repository;

import org.springframework.stereotype.Repository;
import ru.jleague13.entity.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ashevenkov 06.09.15 22:17.
 */
@Repository
public class DbNewsRepository implements NewsRepository {

    private final ArrayList<NewsItem> newsItems;

    public DbNewsRepository() {
        newsItems = new ArrayList<>();
        NewsItem item1 = new NewsItem();
        item1.setId(1);
        item1.setTitle("Новость 1984");
        item1.setText("Сегодня случилось знаковое событие, возвращение блудного попугая. Хонду возглавил Fregll, о же Сержиньё, он же Серёга. В его способностях мы не сомниваемся, однако тяжёлый минус ФМ будет висеть как домоклов меч до самого 15-го тура. Люди добрые помогите чем могёте!!!");
        newsItems.add(item1);
        NewsItem item2 = new NewsItem();
        item2.setId(2);
        item2.setTitle("Новость 2");
        item2.setText("Вот и прошёл второй трансфер межсезонки. На данном этапе наши команды были чуточку активнее. О проданных игроках особо говорить не будем, все подчистили свои ряды, а вот на покупках хотелось бы остановиться по подробнее. И так пойдём по порядку.");
        newsItems.add(item2);
        NewsItem item3 = new NewsItem();
        item3.setId(3);
        item3.setTitle("Новость 3");
        item3.setText("Пока у нас происходят какие-то непонятки с расписанием и оно не появилось на сайте в соответствующем отделе. Пойдём в ручную собирать первый тур чемпионата Японии 30-го сезона и немного прикинем шансы команд.");
        newsItems.add(item3);
    }

    @Override
    public List<NewsItem> lastNews(int count) {
        return newsItems;
    }

    @Override
    public NewsItem getNewsItem(int id) {
        return newsItems.get(id);
    }

    @Override
    public List<NewsItem> lastNewsBefore(int id, int count) {
        return newsItems;
    }
}
