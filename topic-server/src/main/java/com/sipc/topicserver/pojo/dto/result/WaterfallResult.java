package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

import java.util.List;

/**
 * 帖子瀑布流返回类
 * @author o3141
 * @since 2023/4/4 17:05
 * @version 1.0
 */
@Data
public class WaterfallResult {

    /**
     * 帖子列表，不包含作者信息及文本内容
     */
    private List<Waterfall> waterfalls;

    /**
     * 下一次传入的时间戳，用于帖子列表的筛选，以生成瀑布流
     */
    private Long nextTime;

}
