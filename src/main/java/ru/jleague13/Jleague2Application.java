package ru.jleague13;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jleague13.controller.NewsController;

@SpringBootApplication
@Controller
public class Jleague2Application extends SpringBootServletInitializer {

    @Autowired
    private NewsController newsController;

    @RequestMapping("/")
    public String index(Model model) {
        return newsController.getNews(model);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Jleague2Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Jleague2Application.class, args);
    }
}
