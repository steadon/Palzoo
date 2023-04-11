package com.sipc.topicserver.pojo.dto.param;

import lombok.Data;

import java.util.Map;

/**
 * 获取帖子接口的参数，保存了获取帖子所需要的相关参数
 * @author o3141
 * @since 2023/4/3 22:55
 * @version 1.0
 */
@Data
public class SearchParam {

    /**
     * 瀑布流的时间戳，用于返回不同的帖子。<p>
     * 第一次请求时，该参数不需要传入，当请求完成时，返回一个当前时间戳作为下次请求的lastTime
     */
    private Long lastTime;

    /**
     * 筛选集合时间的开始时间
     */
    private String startTime;

    /**
     * 筛选集合时间的结束时间
     */
    private String endTime;

    /**
     * 特定的筛选参数，之后可能删除。<p>
     * 通过map存储，目前其中可存放参数：<p>
     * 1.numberMin，筛选期望人数的最小人数 <p>
     * 2.numberMax，筛选期望人数的最大人数 <p>
     * 3.category，筛选分类 <p>
     */
    private Map<String, String> screen;

}
