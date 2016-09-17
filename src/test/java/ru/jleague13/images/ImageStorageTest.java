package ru.jleague13.images;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ru.jleague13.repository.DbImagesMetaInfoDao;
import ru.jleague13.repository.ImagesMetaInfoDao;

import java.util.Iterator;
import java.util.List;

/**
 * @author ashevenkov 06.09.16 10:40.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageStorageTest {

    private ImagesStorage imagesStorage100 = createImagesStorage100();
    private ImagesStorage imagesStorage18 = createImagesStorage18();

    private ImagesStorage createImagesStorage100() {
        ImagesStorage imagesStorage = new ImagesStorage();
        imagesStorage.setMetaInfoDao(new DbImagesMetaInfoDao() {
            @Override
            public int getImageCount() {
                return 100;
            }
        });
        return imagesStorage;
    }

    private ImagesStorage createImagesStorage18() {
        ImagesStorage imagesStorage = new ImagesStorage();
        imagesStorage.setMetaInfoDao(new DbImagesMetaInfoDao() {
            @Override
            public int getImageCount() {
                return 18;
            }
        });
        return imagesStorage;
    }

    @Test
    public void testPager() {
        List<String> pager = imagesStorage100.pager(0, 0).indexes;
        assert toString(pager).equals("ERROR");
        pager = imagesStorage100.pager(9, 10).indexes;
        assert toString(pager).equals("1 2 3 >");
        pager = imagesStorage100.pager(30, 10).indexes;
        assert toString(pager).equals("< 2 3 4 5 6 >");
        pager = imagesStorage100.pager(90, 10).indexes;
        assert toString(pager).equals("< 8 9 10");
        pager = imagesStorage18.pager(0, 10).indexes;
        assert toString(pager).equals("1 2 >");
    }

    private String toString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            sb.append(next);
            if(iterator.hasNext()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
