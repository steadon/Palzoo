package com.sipc.topicserver.pojo.dto.result;

import lombok.Data;

import java.util.List;

/**
 * 帖子详细信息类，包含了作者信息以及详细的文本内容，及现在已经组队参与的人数
 * @author o3141
 * @since 2023/4/4 14:39
 * @version 1.0
 */
@Data
public class DetailResult {

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子文本内容
     */
    private String content;

    /**
     * 帖子分类
     */
    private String category;

    /**
     * 帖子标签列表
     */
    private List<String> categoryNext;

    /**
     * 作者信息，包含姓名、学校、年龄、性别
     */
    private UserInfo author;

    /**
     * 观看数
     */
    private Integer watchNum;

    /**
     * 帖子所组织活动的期望人数
     */
    private Integer num;

    /**
     * 帖子所组织活动的性别限制
     */
    private String gender;

    /**
     * 集合时间
     */
    private String goTime;

    /**
     * 帖子所租住活动的队伍现在的人数
     */
    private Integer nowNum;

    /**
     * 帖子所组织的活动是否完成
     */
    private Byte isFinish;

}
