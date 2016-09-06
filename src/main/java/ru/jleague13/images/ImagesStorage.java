package ru.jleague13.images;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.jleague13.entity.Pager;
import ru.jleague13.repository.ImagesMetaInfoDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashevenkov 01.09.16 12:28.
 */
@Component
public class ImagesStorage {

    @Autowired
    private ImagesMetaInfoDao metaInfoDao;

    public void setMetaInfoDao(ImagesMetaInfoDao metaInfoDao) {
        this.metaInfoDao = metaInfoDao;
    }

    @Value("${images.dir}")
    private String imagesDir;

    public String saveFile(MultipartFile file) throws IOException {
        int id = 0;
        try {
            ImageInfo imageInfo = new ImageInfo(0, file.getOriginalFilename(), file.getContentType());
            id = metaInfoDao.saveImageInfo(imageInfo);
            imageInfo.id = id;
            new File(imagesDir + "/upload/").mkdir();
            FileOutputStream fw = new FileOutputStream(imagesDir + "/upload/" + id);
            IOUtils.copy(file.getInputStream(), fw);
        } catch (IOException e) {
            metaInfoDao.deleteMetaInfo(id);
            throw e;
        }
        return Integer.toString(id);
    }

    public List<ImageInfo> loadInfos(int skip, int count) {
        return metaInfoDao.getMetaInfos(skip, count);
    }

    public FileInputStream loadFromDisk(int id) {
        ImageInfo imageInfo = metaInfoDao.getMetaInfo(id);
        FileInputStream fis = null;
        if(imageInfo != null) {
            try {
                fis = new FileInputStream(imagesDir + "/upload/" + id);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fis;
    }

    public ImageInfo getImageInfo(int id) {
        return metaInfoDao.getMetaInfo(id);
    }

    public void deleteImage(int id) {
        ImageInfo metaInfo = metaInfoDao.getMetaInfo(id);
        new File(imagesDir + "/upload/" + id).delete();
        metaInfoDao.deleteMetaInfo(id);
    }

    public Pager pager(int skip, int count) {
        List<String> result = new ArrayList<>();
        if(count == 0) {
            result.add("ERROR");
            return new Pager("", result);
        }
        int imageCount = metaInfoDao.getImageCount();
        int pages = (imageCount - 1) / count + 1;
        int page = skip / count + 1;
        if(page > 2) {
            result.add(Integer.toString(page - 2));
        }
        if(page > 1) {
            result.add(0, "<");
            result.add(Integer.toString(page - 1));
        }
        result.add(Integer.toString(page));
        if(page < pages - 2) {
            result.add(Integer.toString(page + 1));
            result.add(">");
        }
        if(page < pages - 1) {
            result.add(result.size() - 1, Integer.toString(page + 2));
        }
        return new Pager(Integer.toString(page), result);
    }
}
