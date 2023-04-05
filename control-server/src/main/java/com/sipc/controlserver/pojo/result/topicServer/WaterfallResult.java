package com.sipc.controlserver.pojo.result.topicServer;

import lombok.Data;

import java.util.List;

/**
 * @ClassName WaterfallResult
 * @Description TODO
 * @Author o3141
 * @Date 2023/4/4 17:05
 * @Version 1.0
 */
@Data
public class WaterfallResult {

    private List<Waterfall> waterfalls;

    private Long nextTime;

}
