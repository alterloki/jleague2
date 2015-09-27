package ru.jleague13.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.Jleague2Application;
import ru.jleague13.entity.NewsArticle;
import ru.jleague13.entity.NewsSnippet;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ashevenkov 10.09.15 15:41.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Jleague2Application.class)
@Transactional
public class NewsDaoTests {

    @Autowired
    private NewsDao newsDao;
    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp() throws ParseException {
        new JdbcTemplate(dataSource).update("delete from users");
        new JdbcTemplate(dataSource).update("delete from news_item");
        new JdbcTemplate(dataSource).update(
                "insert into users (id, login, name) values (?,?,?)",
                1, "asd", "Ivanov Ivan");
        new JdbcTemplate(dataSource).update(
                "insert into users (id, login, name) values (?,?,?)",
                2, "qwe", "Sidorov Sidor");
        new JdbcTemplate(dataSource).update(
                "insert into users (id, login, name) values (?,?,?)",
                3, "zxc", "Vasilyev Vasiliy");
        new JdbcTemplate(dataSource).update(
                "insert into news_item (id, title, text, user_id, post_date) values (?,?,?,?,?)",
                1, "The most interesting story",
                "Josajdojasd asodja asdasd.", 1,
                new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2015 00:27"));
        new JdbcTemplate(dataSource).update(
                "insert into news_item (id, title, text, user_id, post_date) values (?,?,?,?,?)",
                2, "The longest story",
                "Josajdojasd asodja asdasd.dasjksfa" +
                        "sadflks adlkfj;lasdf\n" +
                        " asdj;sdaf afdsadf <b>fasdfasdfsdfasdfasdfasdfasdf</b>" +
                        " fsdfasd. sdfhldsajff sdiushasdf fsdfkghsdkflsdf asdfsdf" +
                        " sadfasdfasdf lsakdhfj lkhdslkj ajksdhf jkadhsflk jhsadljhf" +
                        "asd asdkjfhsakjl hlkjashd kjhsd jkhkjl sajkhasfdjk hsa lsf" +
                        "sdkjf hkskjhas fdkjhjklh afsljk<i>lkj hsjdkfh jkashfk l</i>", 1,
                new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2015 00:50"));
        new JdbcTemplate(dataSource).update(
                "insert into news_item (id, title, text, user_id, post_date) values (?,?,?,?,?)",
                3, "The other story",
                "Josa", 3,
                new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.09.2015 00:50"));

    }

    @After
    public void tearDown() {
        new JdbcTemplate(dataSource).update("delete from news_item");
        new JdbcTemplate(dataSource).update("delete from users");
    }

    @Test
    public void testGetNewsArticle() throws ParseException {
        NewsArticle newsArticle = newsDao.getNewsArticle(3);
        assert newsArticle.getId() == 3;
        assert newsArticle.getAuthor() != null;
        assert newsArticle.getTitle().equals("The other story");
        assert newsArticle.getAuthor().getLogin().equals("zxc");
        assert newsArticle.getAuthor().getName().equals("Vasilyev Vasiliy");
        assert newsArticle.getFullText().equals("Josa");
        assert newsArticle.getDate().getTime() ==
                new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.09.2015 00:50").getTime();
    }

    @Test
    public void testGetSnippets() {
        List<NewsSnippet> newsSnippets = newsDao.lastNewsSnippets(2);
        assert newsSnippets.size() == 2;
        NewsSnippet last = newsSnippets.get(0);
        assert last.getId() == 3;
        assert last.getAuthor() != null;
        assert last.getTitle().equals("The other story");
        assert last.getAuthor().getLogin().equals("zxc");
        assert last.getAuthor().getName().equals("Vasilyev Vasiliy");
        assert last.getShortText().equals("Josa");
        assert last.getPicture() == null;
        NewsSnippet first = newsSnippets.get(1);
        assert first.getId() == 2;
        assert first.getAuthor() != null;
        assert first.getTitle().equals("The longest story");
        assert first.getAuthor().getLogin().equals("asd");
        assert first.getAuthor().getName().equals("Ivanov Ivan");
        assert first.getShortText().length() == 203;
        assert first.getPicture() == null;
    }

    @Test
    public void testGetSnippetsFrom() {
        List<NewsSnippet> newsSnippets = newsDao.lastNewsBefore(1, 2);
        assert newsSnippets.size() == 2;
        NewsSnippet last = newsSnippets.get(0);
        assert last.getId() == 2;
        assert last.getAuthor() != null;
        assert last.getTitle().equals("The longest story");
        assert last.getAuthor().getLogin().equals("asd");
        assert last.getAuthor().getName().equals("Ivanov Ivan");
        assert last.getShortText().length() == 203;
        assert last.getPicture() == null;
        NewsSnippet first = newsSnippets.get(1);
        assert first.getId() == 1;
        assert first.getAuthor() != null;
        assert first.getTitle().equals("The most interesting story");
        assert first.getAuthor().getLogin().equals("asd");
        assert first.getAuthor().getName().equals("Ivanov Ivan");
        assert first.getPicture() == null;
    }
}
