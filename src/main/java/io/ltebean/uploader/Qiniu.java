package io.ltebean.uploader;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

/**
 * Created by leo on 16/5/17.
 */
public class Qiniu {

    String ACCESS_KEY = "k1k_lcCFc9PzpRSnECppmJbl_KDr5rgmXcBncmXn";
    String SECRET_KEY = "bA3ZI0LkcE54FeMSmlw2K2OPrltVv4UYm00Ftyfl";

    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    UploadManager uploadManager = new UploadManager();

    public boolean upload(String bucket, String filePath, String fileName) {
        try {
            String token = auth.uploadToken(bucket);
            //调用put方法上传
            Response res = uploadManager.put(filePath, fileName, token);
            return true;
        } catch (QiniuException e) {
            return false;
        }
    }

}
