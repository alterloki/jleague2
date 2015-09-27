package ru.jleague13.download;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jleague13.entity.Country;
import ru.jleague13.entity.Team;
import ru.jleague13.images.ImagesManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author ashevenkov 16.09.15 15:02.
 */
@Component
public class SimpleUrlDownloadImages implements DownloadImages {

    private Log log = LogFactory.getLog(SimpleUrlDownloadImages.class);

    @Autowired
    private FaUrlResolver urlResolver;
    @Autowired
    private ImagesManager imagesManager;

    @Override
    public void downloadFlag(Country country) throws IOException {
        URL url = new URL(urlResolver.getFaCountryFlag(country));
        log.info("Downloading flag " + urlResolver.getFaCountryFlag(country));
        byte[] pictureBytes = ByteStreams.toByteArray(url.openStream());
        File to = new File(imagesManager.countryFlagFile(country));
        Files.createParentDirs(to);
        Files.write(pictureBytes, to);
    }

    @Override
    public void downloadEmblem(Team team) throws IOException {
        URL url = new URL(urlResolver.getFaTeamEmblem(team));
        log.info("Downloading emblem " + urlResolver.getFaTeamEmblem(team));
        BufferedImage in = ImageIO.read(url.openStream());
        BufferedImage bufferedImage = colorImage(in);
        File to = new File(imagesManager.teamEmblemFile(team));
        Files.createParentDirs(to);
        ImageIO.write(bufferedImage, "png", to);
    }

    private BufferedImage colorImage(BufferedImage image) {
        return imageToBufferedImage(transformColorToTransparency(
                image,
                new Color(88, 115, 142),
                new Color(185, 220, 255)), 100, 100);
    }

    private Image transformColorToTransparency(BufferedImage image, Color c1, Color c2) {
        final int r1 = c1.getRed();
        final int g1 = c1.getGreen();
        final int b1 = c1.getBlue();
        final int r2 = c2.getRed();
        final int g2 = c2.getGreen();
        final int b2 = c2.getBlue();
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                int r = (rgb & 0xFF0000) >> 16;
                int g = (rgb & 0xFF00) >> 8;
                int b = rgb & 0xFF;
                if (r >= r1 && r <= r2 &&
                        g >= g1 && g <= g2 &&
                        b >= b1 && b <= b2) {
                    return rgb & 0xFFFFFF;
                }
                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private BufferedImage imageToBufferedImage(Image image, int width, int height) {
        BufferedImage dest = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }
}
