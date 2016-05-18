package io.ltebean.uploader;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by leo on 16/5/17.
 */

@ConfigurationProperties(prefix="qiniu")
@Component
public class Qiniu {

    @NotEmpty
    public String accessKey;

    @NotEmpty
    public String secretKey;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    UploadManager uploadManager = new UploadManager();

    public boolean upload(String bucket, String filePath, String fileName) {
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String token = auth.uploadToken(bucket);
            uploadManager.put(filePath, fileName, token);
            return true;
        } catch (QiniuException e) {
            return false;
        }
    }

}
