package ru.jleague13.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.jleague13.entity.User;
import ru.jleague13.images.ImageInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ashevenkov 04.09.16 16:30.
 */
@Repository
public class DbImagesMetaInfoDao implements ImagesMetaInfoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static String FIELDS = "id, name, content_type";

    @Override
    public int saveImageInfo(ImageInfo imageInfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement("insert into image_meta_info " +
                            "(name, content_type)" +
                            " values (?,?)", new String[]{"id"});
            ps.setString(1, imageInfo.name);
            ps.setString(2, imageInfo.contentType);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void deleteMetaInfo(int id) {
        jdbcTemplate.update("delete from image_meta_info where id = ?", id);
    }

    @Override
    public ImageInfo getMetaInfo(int id) {
        return jdbcTemplate.query("select " + FIELDS + " from image_meta_info where id = ?",
                rs -> {
                    if(rs.next()) {
                        return imageInfoFromResultSet(rs);
                    }
                    return null;
                }, id);
    }

    private ImageInfo imageInfoFromResultSet(ResultSet rs) throws SQLException {
        return new ImageInfo(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("content_type"));
    }

    @Override
    public List<ImageInfo> getMetaInfos(int skip, int count) {
        return jdbcTemplate.query("select " + FIELDS + " from (select * from image_meta_info order by id desc) v1 limit ?, ?",
                (rs, i) -> imageInfoFromResultSet(rs), skip, count);
    }

    @Override
    public int getImageCount() {
        return jdbcTemplate.queryForObject("select count(1) from image_meta_info", Integer.class);
    }
}
