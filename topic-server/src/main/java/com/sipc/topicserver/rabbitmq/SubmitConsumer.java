package com.sipc.topicserver.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.topicserver.config.DirectRabbitConfig;
import com.sipc.topicserver.constant.Constant;
import com.sipc.topicserver.mapper.CategoryMapper;
import com.sipc.topicserver.mapper.PostMapper;
import com.sipc.topicserver.pojo.domain.Category;
import com.sipc.topicserver.pojo.domain.Post;
import com.sipc.topicserver.pojo.dto.param.SubmitParam;
import com.sipc.topicserver.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author o3141
 * @since 2023/4/4 18:59
 * @version 1.0
 */
@Component
@Slf4j
public class SubmitConsumer {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private RedisUtil redisUtil;

    @RabbitListener(queues = DirectRabbitConfig.QUEUE_NAME)
    public void consumer(SubmitParam submitParam) {

        //获取分类id
        Integer categoryId = (Integer)redisUtil.get("categoryName:" + submitParam.getCategory());

        if (categoryId == null) {
            Category category1 = categoryMapper.selectOne(
                    new QueryWrapper<Category>()
                            .eq("name", submitParam.getCategory())
                            .last("limit 1")
            );
            if (category1 == null) {
                log.warn("未查到正确的分类id，查询分类名称： {}", submitParam.getCategory());
                return;
            }
            categoryId = category1.getId();
            redisUtil.set("categoryName:" + submitParam.getCategory(), categoryId);
        }

        //获取子标签id
        Integer authorId = submitParam.getUserId();

        Post post = new Post();

        //设置post类数据
        post.setTitle(submitParam.getTitle());
        post.setContent(submitParam.getContext());
        post.setAuthorId(authorId);
        post.setCategoryId(categoryId);

        StringBuilder categoryNext = new StringBuilder();
        int i = 1;
        if (submitParam.getCategoryNext() != null) {
            int len = submitParam.getCategoryNext().size();
            for (String s : submitParam.getCategoryNext()) {
                if (i < len) {
                    categoryNext.append(s).append("+");
                } else {
                    categoryNext.append(s);
                }
                ++i;
            }
        }

        post.setCategoryNext(categoryNext.toString());
        post.setGender(submitParam.getGender());
        post.setNumber(submitParam.getNumber());
        post.setIsFinish((byte) 0);
        post.setGoTime(LocalDateTime.parse(submitParam.getGoTime(), Constant.dateTimeFormatter));
        post.setWatchNum(0);
        post.setUpdatedTime(LocalDateTime.now());
        post.setCreatedTime(LocalDateTime.now());
        post.setIsDeleted((byte) 0);

        //插入数据
        int insert = postMapper.insert(post);

        if (insert != 1) {
            log.error("数据库操作异常，帖子提交操作失败，插入数据数：{}, 插入帖子为： {}", insert, post);
        }

        log.info("成功提交帖子， 帖子数据为： {}", post.toString());

    }

}
