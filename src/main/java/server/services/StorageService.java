package server.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class StorageService {

    @Value("${jsa.s3.bucket}")
    private String bucketName;

    private final AmazonS3 client;

    @Autowired
    public StorageService(AmazonS3 client) {
        this.client = client;
    }

    public void uploadFile(@NotNull String keyName, @NotNull String data) {
        try {
            Path uploadFilePath = Files.createTempFile(keyName, null);
            byte[] bytes = Base64.getDecoder().decode(data);
            Files.write(uploadFilePath, bytes);
            var request = new PutObjectRequest(bucketName, keyName, uploadFilePath.toFile());
            request = request.withCannedAcl(CannedAccessControlList.PublicRead);
            client.putObject(request);
        } catch (IOException ignored) {}
    }

//    public String downloadFile(@NotNull String keyName) {
//        S3Object object = client.getObject(bucketName, keyName);
//        try {
//            byte[] bytes = object.getObjectContent().readAllBytes();
//            return Base64.getEncoder().encodeToString(bytes);
//        } catch (IOException ignored) {}
//        return null;
//    }

    public void removeFile(@NotNull String keyName) {
        client.deleteObject(bucketName, keyName);
    }
}
