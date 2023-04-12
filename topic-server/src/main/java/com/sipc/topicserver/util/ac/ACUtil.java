package com.sipc.topicserver.util.ac;

import com.sipc.topicserver.constant.Constant;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/12 16:25
 */
@Component
public class ACUtil {

    @Resource
    private RestTemplate restTemplate;

    public ACUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ACResult check(String title, String content) {
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));

        //设置请求体
        ACParam acParam = new ACParam();
        acParam.setTitle(title);
        acParam.setContent(content);

        // 创建请求实体对象
        HttpEntity<ACParam> entity = new HttpEntity<>(acParam, httpHeaders);

        ResponseEntity<ACResult> acResultResponseEntity = restTemplate.postForEntity(Constant.checkURL, entity, ACResult.class);

        if (acResultResponseEntity.getStatusCode() == HttpStatus.OK) {
//            System.out.println(acResultResponseEntity.getBody().toString());
            return acResultResponseEntity.getBody();
        }

        return null;
    }

}
