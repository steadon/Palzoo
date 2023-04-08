package com.sipc.controlserver.pojo.result.topicServer;

import lombok.Data;

import java.util.List;

/**
 * @author o3141
 * @since 2023/4/4 17:05
 * @version 1.0
 */
@Data
public class WaterfallResult {

    private List<Waterfall> waterfalls;

    private Long nextTime;

}
