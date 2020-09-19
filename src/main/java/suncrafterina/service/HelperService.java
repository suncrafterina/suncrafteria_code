package suncrafterina.service;

import com.github.slugify.Slugify;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Status;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

@Service
public class HelperService {

    public static double bytesToKB(double bytes){
        return  (bytes / 1024.0);
    }
    private static final Random generator = new Random();

    public static String generatePin() {

        return new StringBuilder().append(100000 + generator.nextInt(900000)).toString();
    }

    public MultipartFile createThumbnail(MultipartFile orginalFile) throws IOException {
        String extension = getImageExtension(orginalFile.getContentType());
        String folderName ="./"+ System.currentTimeMillis();
        File dir = new File(folderName);
        if(!dir.exists())
            dir.mkdir();
        DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeString = LocalDateTime.now().format(DATE_TIME_FORMAT).toString();
        String filename = StringUtils.cleanPath(timeString+"."+extension);
        try {
            try (InputStream inputStream = orginalFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(folderName+"/").resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception ex){
                System.out.println(ex.getMessage()+" "+ex.getLocalizedMessage());
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
        }catch(Exception ex) {
            FileUtils.deleteDirectory(dir);
            System.out.println(ex.getMessage()+" "+ex.getLocalizedMessage());
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
        }
        Thumbnails.of(new File(folderName+"/"+filename)).size(200, 200).outputFormat(extension).toFile(new File(folderName+"/"+timeString));
        Path path = Paths.get(folderName+"/"+filename);
        String name = filename;
        String originalFileName = orginalFile.getOriginalFilename();
        String contentType = orginalFile.getContentType();
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
        }
        MultipartFile result = new MockMultipartFile(name,originalFileName, contentType, content);
        FileUtils.deleteDirectory(dir);
        return result;
    }

    public static String getImageExtension(String contentType) {
        String[] array = contentType.split("/");
        return array[array.length-1];
    }

    public static String getUnderscoreSlug(String input){
        Slugify slg = new Slugify();
        slg.withUnderscoreSeparator(true);
        String result = slg.slugify(input);
        return result;
    }

    public MultipartFile createBannerThumbnail(MultipartFile orginalFile) throws IOException {
        String extension = getImageExtension(orginalFile.getContentType());
        String folderName ="./"+ System.currentTimeMillis();
        File dir = new File(folderName);
        if(!dir.exists())
            dir.mkdir();
        DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeString = LocalDateTime.now().format(DATE_TIME_FORMAT).toString();
        String filename = StringUtils.cleanPath(timeString+"."+extension);
        try {
            try (InputStream inputStream = orginalFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(folderName+"/").resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);

            }

        }catch(Exception ex) {
            System.out.println(ex.getMessage()+" "+ex.getLocalizedMessage());
        }
        Thumbnails.of(new File(folderName+"/"+filename)).size(400, 400).outputFormat(extension).toFile(new File(folderName+"/"+timeString));
        Path path = Paths.get(folderName+"/"+filename);
        String name = filename;
        String originalFileName = orginalFile.getOriginalFilename();
        String contentType = orginalFile.getContentType();
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name,originalFileName, contentType, content);
        FileUtils.deleteDirectory(dir);
        return result;
    }
    public  static String getSlug(String input){
        Slugify slg = new Slugify();
        String result = slg.slugify(input);
        return result;
    }

}
