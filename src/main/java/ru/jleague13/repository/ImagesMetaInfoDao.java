package ru.jleague13.repository;

import ru.jleague13.images.ImageInfo;

import java.util.List;

/**
 * @author ashevenkov 01.09.16 12:25.
 */
public interface ImagesMetaInfoDao {


    int saveImageInfo(ImageInfo imageInfo);

    void deleteMetaInfo(int id);

    ImageInfo getMetaInfo(int id);

    List<ImageInfo> getMetaInfos(int skip, int count);

    int getImageCount();

}
