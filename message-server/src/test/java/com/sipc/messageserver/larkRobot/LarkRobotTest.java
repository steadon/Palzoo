package com.sipc.messageserver.larkRobot;

import com.sipc.messageserver.utils.lark.LarkRobot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 15:02
 */
@SpringBootTest
public class LarkRobotTest {

    @Resource
    private LarkRobot larkRobot;

    @Test
    public void test() {
        System.out.println(larkRobot.toString());
    }

}
