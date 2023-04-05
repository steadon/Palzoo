package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

import java.util.List;

/**
 * ClassName WaterfallResult
 * Description
 * Author o3141
 * Date 2023/4/4 17:05
 * Version 1.0
 */
@Data
public class WaterfallResult {

    private List<Waterfall> waterfalls;

    private Long nextTime;

}
