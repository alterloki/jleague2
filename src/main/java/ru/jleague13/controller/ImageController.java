package ru.jleague13.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.jleague13.images.ImageInfo;
import ru.jleague13.images.ImagesStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author ashevenkov 03.09.16 11:05.
 */
@Controller
public class ImageController {

    private Log log = LogFactory.getLog(ImageController.class);

    @Autowired
    private ImagesStorage imagesStorage;

    @RequestMapping(value="/new/admin/image/upload", method = RequestMethod.POST)
    public String uploadImage(MultipartHttpServletRequest request, Model model) {
        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = null;
        while(itr.hasNext()){
            mpf = request.getFile(itr.next());
            try {
                imagesStorage.saveFile(mpf);
            } catch (IOException e) {
                log.error("Error saving file " + mpf.getName());
                return "error";
            }
        }
        return "ok";
    }

    @RequestMapping(value="/new/admin/image/{id}", method = RequestMethod.POST)
    public void getImage(@PathVariable int id, HttpServletResponse response) {

    }

    @RequestMapping(value="/new/admin/images", method = RequestMethod.GET)
    public String getAdminImages() {
        return "admin/images";
    }

    @RequestMapping(value="/new/admin/photos", method = RequestMethod.GET)
    public String getAdminImagesTable(Model model, HttpServletRequest request) {
        String countS = request.getParameter("count");
        int count = 10;
        if(countS != null) {
            count = Integer.parseInt(countS);
        }
        String skipS = request.getParameter("skip");
        int skip = 0;
        if(skipS != null) {
            skip = Integer.parseInt(skipS);
        }
        model.addAttribute("images", imagesStorage.loadInfos(skip, count));
        model.addAttribute("pager", imagesStorage.pager(skip, count));
        return "admin/photos";
    }

    @RequestMapping(value="/new/images")
    public String getImages(Model model) {
        return "";
    }

    @RequestMapping(value="/new/admin/images/{id}", method = RequestMethod.DELETE)
    public String getImages(@PathVariable int id, Model model, HttpServletRequest request) {
        imagesStorage.deleteImage(id);
        return "ok";
    }
}
