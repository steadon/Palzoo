package com.sipc.topicserver.openfeign;

import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.result.teamServer.GetTeamIdResult;
import com.sipc.topicserver.pojo.dto.result.teamServer.GetTeamInfoResult;
import com.sipc.topicserver.pojo.dto.resultEnum.ResultEnum;
import com.sipc.topicserver.service.openfeign.TeamServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/11 15:16
 */
@SpringBootTest
public class TeamTest {

    @Resource
    private TeamServer teamServer;

    @Test
    public void test() {

            CommonResult<GetTeamIdResult> teamIdResult = teamServer.getTeamId(1);
            if (!Objects.equals(teamIdResult.getCode(), ResultEnum.SUCCESS.getCode())) {
                return;
            }
            if (teamIdResult.getData() != null && teamIdResult.getData().getTeamId() != null) {
                CommonResult<GetTeamInfoResult> teamInfoResult = teamServer.getTeamInfo(teamIdResult.getData().getTeamId());
                if (!Objects.equals(teamInfoResult.getCode(), ResultEnum.SUCCESS.getCode())) {
                    return;
                }
                if (teamInfoResult.getData() != null && teamInfoResult.getData().getActNum() != null) {
                    System.out.println( teamInfoResult.getData().getActNum());;
                }
            }


    }

}
