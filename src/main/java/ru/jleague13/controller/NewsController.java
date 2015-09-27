package ru.jleague13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.jleague13.repository.NewsDao;
import ru.jleague13.repository.TeamDao;

/**
 * @author ashevenkov 06.09.15 21:40.
 */
@Controller
@RequestMapping("/news")
public class NewsController {

    public static final int PAGE_NEWS_COUNT = 5;

    @Autowired
    private NewsDao newsRepository;

    @Autowired
    private TeamDao teamDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getNews(Model model) {
        model.addAttribute("news", newsRepository.lastNewsSnippets(PAGE_NEWS_COUNT));
        model.addAttribute("japanTeams", teamDao.getJapanLiveTeams());
        return "newsList";
    }
}
