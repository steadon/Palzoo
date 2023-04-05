package com.sipc.topicserver.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.topicserver.config.DirectRabbitConfig;
import com.sipc.topicserver.mapper.CategoryMapper;
import com.sipc.topicserver.mapper.PostMapper;
import com.sipc.topicserver.pojo.domain.Category;
import com.sipc.topicserver.pojo.domain.Post;
import com.sipc.topicserver.pojo.dto.param.SubmitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ClassName SubmitConsumer
 * Description
 * Author o3141
 * Date 2023/4/4 18:59
 * Version 1.0
 */
@Component
@Slf4j
public class SubmitConsumer {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private PostMapper postMapper;

    @RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME)
    public void consumer(SubmitParam submitParam) {

        //获取分类id
        Category category = categoryMapper.selectOne(
                new QueryWrapper<Category>()
                        .eq("name", submitParam.getCategory())
                        .last("limit 1")
        );

        if (category == null) {
            return;
        }

        Integer authorId = 0;

        Post post = new Post();

        //设置post类数据
        post.setTitle(submitParam.getTitle());
        post.setBrief(submitParam.getBrief());
        post.setContent(submitParam.getContext());
        post.setAuthorId(authorId);
        post.setCategoryId(category.getId());
        post.setIsFinish((byte) 0);
        post.setStartTime(LocalDateTime.parse(submitParam.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        post.setEndTime(LocalDateTime.parse(submitParam.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        post.setWatchNum(0);
        post.setUpdatedTime(LocalDateTime.now());
        post.setCreatedTime(LocalDateTime.now());
        post.setIsDeleted((byte) 0);

        //插入数据
        int insert = postMapper.insert(post);

        if (insert != 1) {
            log.error("数据库操作异常，帖子提交操作失败，插入数据数：{}", insert);
        }

    }

}
