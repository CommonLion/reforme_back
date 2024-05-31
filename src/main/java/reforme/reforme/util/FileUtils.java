package reforme.reforme.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtils {

    public MultipartFile convertFileToMultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        return new CustomMultipartFile(file, file.getName());
    }
}
