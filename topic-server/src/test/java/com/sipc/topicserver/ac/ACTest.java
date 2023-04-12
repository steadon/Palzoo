package com.sipc.topicserver.ac;

import com.sipc.topicserver.pojo.dto.param.messageServer.SendParam;
import com.sipc.topicserver.service.openfeign.MessageServer;
import com.sipc.topicserver.util.ac.ACResult;
import com.sipc.topicserver.util.ac.ACUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/12 16:57
 */
@SpringBootTest
public class ACTest {

    @Resource
    private ACUtil acUtil;

    @Resource
    private MessageServer messageServer;

    @Test
    public void test() {

        String title = "tm的";
        String content = "fuck， 你好吗";

        Integer userId = 1;

        //检验敏感词
        ACResult check = acUtil.check(title, content);
        System.out.println(check.toString());

        if (check != null && check.getStatusCode() == 0 ) {
            if (check.getTitleList() != null || check.getContentList() != null) {

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("您的帖子含有违禁词语，请您修正。</br>违禁词语如下：</br>");

                if (check.getTitleList() != null) {
                    stringBuilder.append("标题：");
                    stringBuilder.append(check.getTitleList());
                    stringBuilder.append("</br>");
                }

                if (check.getContentList() != null) {
                    stringBuilder.append("文本：");
                    stringBuilder.append(check.getContentList());
                    stringBuilder.append("</br>");
                }

                SendParam sendParam = new SendParam();

                sendParam.setUserId(0);
                sendParam.setToUserId(userId);
                sendParam.setContent(stringBuilder.toString());

                //捕获异常，调用openfeign发送消息可能导致异常导致消息被重复消费
//                try {
//                    messageServer.send(sendParam);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                return;
            }
        }
    }

}
