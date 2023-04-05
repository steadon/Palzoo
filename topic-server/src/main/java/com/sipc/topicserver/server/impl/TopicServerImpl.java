package com.sipc.topicserver.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sipc.topicserver.config.DirectRabbitConfig;
import com.sipc.topicserver.constant.Constant;
import com.sipc.topicserver.mapper.CategoryMapper;
import com.sipc.topicserver.mapper.CategoryNextMapper;
import com.sipc.topicserver.mapper.PostMapper;
import com.sipc.topicserver.pojo.domain.Category;
import com.sipc.topicserver.pojo.domain.CategoryNext;
import com.sipc.topicserver.pojo.domain.Post;
import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.mic.result.GetUserInfoResult;
import com.sipc.topicserver.pojo.dto.param.*;
import com.sipc.topicserver.pojo.dto.result.DetailResult;
import com.sipc.topicserver.pojo.dto.result.Waterfall;
import com.sipc.topicserver.pojo.dto.result.WaterfallResult;
import com.sipc.topicserver.server.TopicServer;
import com.sipc.topicserver.server.openfeign.UserServer;
import com.sipc.topicserver.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName TopicServerImpl
 * Description topic服务的处理类
 * Author o3141
 * Date 2023/4/3 22:09
 * Version 1.0
 */
@Service
@Slf4j

public class TopicServerImpl implements TopicServer {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    public PostMapper postMapper;

    @Resource
    private CategoryNextMapper categoryNextMapper;

    //找不到UserServer的been
    @Resource
    private UserServer userServer;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public synchronized CommonResult<String> submit(SubmitParam submitParam) {

        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME, DirectRabbitConfig.ROUTING_KEY, submitParam);

        return CommonResult.success("提交成功，审核中");

    }

    @Override
    public CommonResult<WaterfallResult> search(SearchParam searchParam) {

        WaterfallResult waterfallResult = new WaterfallResult();

        List<Waterfall> waterfalls = new ArrayList<>();

        //获取筛选项
        Map<String, String> screen = searchParam.getScreen();

        //单独处理分类筛选项
        String category = screen.get("category");

        if (category != null) {
            Category category1 = categoryMapper.selectOne(
                    new QueryWrapper<Category>()
                            .eq("name", category)
                            .last("limit 1")
            );
            if (category1 == null) {
                return CommonResult.fail("分类错误");
            }

            screen.remove("category");
            //输入筛选项
            screen.put("category_id", category1.getId().toString());
        }

        //单独处理子标签
        List<Integer> categoryNextIds = new ArrayList<>();

        String categoryNext = screen.get("categoryNext");
        if (categoryNext != null) {
            //categoryNext以&为分隔符
            for (String s : categoryNext.split("&")) {
                CategoryNext name = categoryNextMapper.selectOne(
                        new QueryWrapper<CategoryNext>()
                                .select("id")
                                .eq("name", s)
                                .last("limit 1")
                );
                categoryNextIds.add(name.getId());
            }
            screen.remove("categoryNext");
        }

        if (searchParam.getLastTime() == null) {
            searchParam.setLastTime(LocalDateTime.now().toEpochSecond(Constant.zoneOffset));
        }

        //
        LocalDateTime nextTime = LocalDateTime.now();


        List<Post> postList = new ArrayList<>();

        //如果没有子标签
        if (categoryNextIds.isEmpty()) {
            postList = postMapper.selectList(
                    new QueryWrapper<Post>()
                            .allEq(screen)
                            .lt("created_time", searchParam.getLastTime())
                            .ge("stop_time", LocalDateTime.now().toEpochSecond(Constant.zoneOffset))
                            .ge("start_time", LocalDateTime.parse(searchParam.getStartTime(),
                                    Constant.dateTimeFormatter))
                            .lt("start_time", LocalDateTime.parse(searchParam.getEndTime(),
                                    Constant.dateTimeFormatter))
                            .orderByDesc("created_time")
                            .last("limit 10")
            );
        }
        else {
            postList = postMapper.selectList(
                    new QueryWrapper<Post>()
                            .allEq(screen)
                            .in("category_next_id", categoryNextIds)
                            .lt("created_time", searchParam.getLastTime())
                            .ge("stop_time", LocalDateTime.now().toEpochSecond(Constant.zoneOffset))
                            .ge("start_time", LocalDateTime.parse(searchParam.getStartTime(),
                                    Constant.dateTimeFormatter))
                            .lt("start_time", LocalDateTime.parse(searchParam.getEndTime(),
                                    Constant.dateTimeFormatter))
                            .orderByDesc("created_time")
                            .last("limit 10")
            );
        }

        //循环填充内容
        for (Post post : postList) {

            //redis获取用户信息
            GetUserInfoResult author = this.getAuthor(post.getAuthorId());
            if (author == null) {
                return CommonResult.fail("操作错误，无用户");
            }

            //组装帖子信息
            Waterfall waterfall = new Waterfall();

            waterfall.setPostId(post.getId());
            waterfall.setTitle(post.getTitle());
            waterfall.setBrief(post.getBrief());
            waterfall.setAuthorName(author.getUsername());
            waterfall.setWatchNum(post.getWatchNum());
            waterfall.setStopTime(post.getStopTime().format(Constant.dateTimeFormatter));
            waterfall.setStartTime(post.getStartTime().format(Constant.dateTimeFormatter));
            waterfall.setEndTime(post.getEndTime().format(Constant.dateTimeFormatter));

            waterfalls.add(waterfall);

            nextTime = post.getCreatedTime();
        }

        waterfallResult.setWaterfalls(waterfalls);
        //nextTime.toEpochSecond(ZoneOffset.ofTotalSeconds(8))
        waterfallResult.setNextTime(nextTime.toEpochSecond(Constant.zoneOffset));
        return CommonResult.success(waterfallResult);
    }

    @Override
    public CommonResult<String> finish(FinishParam finishParam) {
        Post post = new Post();

        Integer authorId = 0;

        post.setId(finishParam.getPostId());
        post.setAuthorId(authorId);
        post.setIsFinish((byte) 1);
        post.setUpdatedTime(LocalDateTime.now());
        int i = postMapper.updateById(post);

        if (i != 1) {
            log.error("数据库操作异常，帖子完成接口更新异常，更新数：{}", i);
            return CommonResult.fail("更新失败");
        }

        return CommonResult.success("更新成功");
    }

    @Override
    public CommonResult<DetailResult> detail(Integer postId) {
        Post post = postMapper.selectOne(
                new QueryWrapper<Post>()
                        .eq("id", postId)
                        .last("limit 1")
        );

        int update = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", postId)
                        .setSql("`watch_num` = `watch_num` + 1 ")
        );
        if (update != 1) {
            return CommonResult.fail("浏览失败");
        }

        //redis获取用户信息
        GetUserInfoResult author = this.getAuthor(post.getAuthorId());

        if (author == null) {
            return CommonResult.fail("操作异常");
        }

        DetailResult result = new DetailResult();

        result.setTitle(post.getTitle());
        result.setBrief(post.getBrief());
        result.setContent(post.getContent());
        result.setAuthorName(author.getUsername());
        result.setWatchNum(post.getWatchNum());
        result.setStopTime(post.getStopTime().format(Constant.dateTimeFormatter));
        result.setStartTime(post.getStartTime().format(Constant.dateTimeFormatter));
        result.setEndTime(post.getEndTime().format(Constant.dateTimeFormatter));
        result.setIsFinish(post.getIsFinish());

        return CommonResult.success(result);
    }

    @Override
    public CommonResult<WaterfallResult> author(Integer authorId, Long lastTime) {

        WaterfallResult result = new WaterfallResult();

        List<Waterfall> waterfalls = new ArrayList<>();

        //
        LocalDateTime nextTime = LocalDateTime.now();

        //循环填充内容
        for (Post post : postMapper.selectList(
                new QueryWrapper<Post>()
                        .eq("author_id", authorId)
                        .lt("created_time", lastTime)
                        .last("limit 10")
        )) {

            //redis获取用户信息
            GetUserInfoResult author = this.getAuthor(post.getAuthorId());

            if (author == null) {
                return CommonResult.fail("操作异常");
            }

            //组装帖子信息
            Waterfall waterfall = new Waterfall();

            waterfall.setPostId(post.getId());
            waterfall.setTitle(post.getTitle());
            waterfall.setBrief(post.getBrief());
            waterfall.setAuthorName(author.getUsername());
            waterfall.setWatchNum(post.getWatchNum());
            waterfall.setStopTime(post.getStopTime().format(Constant.dateTimeFormatter));
            waterfall.setStartTime(post.getStartTime().format(Constant.dateTimeFormatter));
            waterfall.setEndTime(post.getEndTime().format(Constant.dateTimeFormatter));

            waterfalls.add(waterfall);

            nextTime = post.getCreatedTime();
        }

        result.setWaterfalls(waterfalls);
        result.setNextTime(nextTime.toEpochSecond(ZoneOffset.ofHours(8)));

        return CommonResult.success(result);
    }

    @Override
    public CommonResult<String> delete(DeleteParam deleteParam) {

        int update = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", deleteParam.getPostId())
                        .eq("author_id", deleteParam.getAuthorId())
                        .eq("updated_time", LocalDateTime.now())
                        .set("is_deleted", 1));
        if (update != 1) {
            return CommonResult.fail("删除失败");
        }

        return CommonResult.success("删除成功");
    }

    @Override
    public CommonResult<String> delay(DelayParam delayParam) {

        int update = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", delayParam.getPostId())
                        .set("stop_time", delayParam.getTime())
        );
        if (update != 1) {
            log.info("延迟帖子失败，数据库更新数异常，为 {}" , update);
            return CommonResult.fail("操作失败");
        }

        return CommonResult.success("延迟成功");
    }

    private GetUserInfoResult getAuthor(Integer authorId) {
        //redis获取用户信息
        GetUserInfoResult author = (GetUserInfoResult)redisUtil.get("userId" + authorId);

        //如果redis中没有用户信息，使用openfeign调用user-server的服务获取用户详细信息
        if (author == null ) {
            CommonResult<GetUserInfoResult> serverUserInfo = userServer.getUserInfo(authorId);
            if (Objects.equals(serverUserInfo.getCode(), "A0400")) {
                log.info("查询用户无信息，查询用户id {}", authorId);
                return null;
            }
            author = serverUserInfo.getData();
            redisUtil.set("userId" + authorId, author);
        }
        return author;
    }
}
