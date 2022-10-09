package top.cerbur.graduation.framework.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Slf4j
public class OssClient {

    Configuration cfg = new Configuration(Region.huanan());
    UploadManager uploadManager = new UploadManager(cfg);
    private String accessKey;

    private String secretKey;

    private String bucket;

    private String url;

    @Value("${oss.accessKey}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Value("${oss.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${oss.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }


    @Value("${oss.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    public String uploadImage(String base64Image) throws QiniuException {
        String key = DigestUtils.sha256Hex(base64Image);
        byte[] base64 = Base64.getDecoder().decode(base64Image);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
//            Response response =
            uploadManager.put(base64, key, upToken, null, "image/jpeg", true);

//            解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            throw ex;
        }
        return this.url + key + "?imageMogr2/auto-orient";
    }

}
