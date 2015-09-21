package ru.jleague13.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.Jleague2Application;

/**
 * @author ashevenkov 10.09.15 15:41.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Jleague2Application.class)
@Transactional
public class NewsRepositoryTests extends AbstractTransactionalJUnit4SpringContextTests {

    @Test
    public void testGetNews() {

    }
}
