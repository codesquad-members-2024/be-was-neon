package http.request;

import static utils.Constant.IS_INVALID_FILE_PATH;

import java.io.File;
import java.io.FileNotFoundException;
import utils.DirectoryMatcher;

public class FilePath {
    private final String originalFilePath;
    private final String filePath;

    public FilePath(String filePath) throws FileNotFoundException {
        this.originalFilePath = filePath;
        this.filePath = DirectoryMatcher.mathDirectory(filePath);
        if (!isValidFilePath(this.filePath) && !isCreateCommand(originalFilePath)) {
            throw new FileNotFoundException(IS_INVALID_FILE_PATH);
        }
    }

    private boolean isValidFilePath(String filePath) {
        File file = new File(filePath);
        return file.isFile() && file.exists();
    }

    private boolean isCreateCommand(String originalFilePath) {
        return originalFilePath.startsWith("/user/create");
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
