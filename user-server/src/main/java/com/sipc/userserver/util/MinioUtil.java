package com.sipc.userserver.util;

import com.sipc.userserver.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

public class MinioUtil {

    public static final String URLPrefix = "https://www.haorui.xyz/palzoo/";
    public static final String URLEnd = ".png";


    public static String getPictureURL(String hash){
        return URLPrefix + hash + URLEnd;
    }

    public static String hashFile(Integer userId){
        return DigestUtils.md5DigestAsHex(("HASH" + userId + LocalDateTime.now()).getBytes());

    }
}
