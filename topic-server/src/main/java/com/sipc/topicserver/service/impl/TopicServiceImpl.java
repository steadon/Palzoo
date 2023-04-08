package com.sipc.topicserver.service.impl;

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
import com.sipc.topicserver.service.TopicService;
import com.sipc.topicserver.service.openfeign.UserServer;
import com.sipc.topicserver.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author o3141
 * @since 2023/4/3 22:09
 * @version 1.0
 */
@Service
@Slf4j

public class TopicServiceImpl implements TopicService {

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
        rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME, DirectRabbitConfig.ROUTING_KEY, submitParam,
                //配置死信队列，消息过期时间5s
                message -> {
                    message.getMessageProperties().setExpiration("5000");
                    return message;
                }
        );

        return CommonResult.success("提交成功，审核中");

    }

    @Override
    public CommonResult<WaterfallResult> search(SearchParam searchParam) {

        WaterfallResult waterfallResult = new WaterfallResult();

        List<Waterfall> waterfalls = new ArrayList<>();

        //获取筛选项
        Map<String, String> screen = searchParam.getScreen();

        //查询值
        List<Integer> categoryNextIds = new ArrayList<>();
        int numberMinEq = 0;
        int numberMaxEq = 99999;

        if (searchParam.getScreen() != null) {
            //单独处理分类筛选项
            String category = screen.get("category");

            if (category != null) {

                Integer categoryId = (Integer)redisUtil.get("categoryName:" + category);

                if (categoryId == null) {
                    Category category1 = categoryMapper.selectOne(
                            new QueryWrapper<Category>()
                                    .eq("name", category)
                                    .last("limit 1")
                    );
                    if (category1 == null) {
                        log.warn("未查到正确的分类id，查询分类名称： {}", category);
                        return CommonResult.fail("分类错误");
                    }
                    categoryId = category1.getId();
                    redisUtil.set("categoryName:" + category, categoryId);
                }

                screen.remove("category");
                //输入筛选项
                screen.put("category_id", categoryId.toString());
            }

            //单独处理子标签


            String categoryNext = screen.get("categoryNext");
            if (categoryNext != null) {
                //categoryNext以&为分隔符
                for (String s : categoryNext.split("&")) {

                    Integer categoryNextId = (Integer)redisUtil.get("categoryNextName:" + s);

                    if (categoryNextId == null) {
                        CategoryNext name = categoryNextMapper.selectOne(
                                new QueryWrapper<CategoryNext>()
                                        .select("id")
                                        .eq("name", s)
                                        .last("limit 1")
                        );
                        if (name == null) {
                            log.warn("未查到正确的分类标签id，查询分类标签名称： {}", s);
                            return CommonResult.fail("分类标签错误");
                        }
                        categoryNextId = name.getId();
                        redisUtil.set("categoryNextName:" + s, categoryNextId);
                    }
                    categoryNextIds.add(categoryNextId);
                }
                screen.remove("categoryNext");
            }

            //单独处理最少人数
            String numberMin = screen.get("numberMin");

            if (numberMin != null) {
                screen.remove("numberMin");
                numberMinEq = Integer.parseInt(numberMin);
            }

            //单独处理最多人数
            String numberMax = screen.get("numberMax");

            if (numberMax != null) {
                screen.remove("numberMax");
                numberMaxEq = Integer.parseInt(numberMax);
            }
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
                            .lt("created_time", LocalDateTime.ofEpochSecond(searchParam.getLastTime(),0,Constant.zoneOffset))
                            .ge("end_time", LocalDateTime.now())
                            .ge("go_time", LocalDateTime.parse(searchParam.getStartTime(),
                                    Constant.dateTimeFormatter))
                            .lt("go_time", LocalDateTime.parse(searchParam.getEndTime(),
                                    Constant.dateTimeFormatter))
                            .ge("number", numberMinEq)
                            .le("number", numberMaxEq)
                            .orderByDesc("created_time")
                            .last("limit 10")
            );
        }
        else {
            postList = postMapper.selectList(
                    new QueryWrapper<Post>()
                            .allEq(screen)
                            .in("category_next_id", categoryNextIds)
                            .lt("created_time", LocalDateTime.ofEpochSecond(searchParam.getLastTime(),0,Constant.zoneOffset))
                            .ge("end_time", LocalDateTime.now().toEpochSecond(Constant.zoneOffset))
                            .ge("go_time", LocalDateTime.parse(searchParam.getStartTime(),
                                    Constant.dateTimeFormatter))
                            .lt("go_time", LocalDateTime.parse(searchParam.getEndTime(),
                                    Constant.dateTimeFormatter))
                            .ge("number", numberMinEq)
                            .le("number", numberMaxEq)
                            .orderByDesc("created_time")
                            .last("limit 10")
            );
        }

        //循环填充内容
        for (Post post : postList) {

            //redis获取用户信息
//            GetUserInfoResult author = this.getAuthor(post.getAuthorId());
//            if (author == null) {
//                return CommonResult.fail("操作错误，无用户");
//            }

            //组装帖子信息
            waterfalls.add(setWaterfall(post));

            nextTime = post.getCreatedTime();
        }

        waterfallResult.setWaterfalls(waterfalls);
        //nextTime.toEpochSecond(ZoneOffset.ofTotalSeconds(8))
        waterfallResult.setNextTime(nextTime.toEpochSecond(Constant.zoneOffset));
        return CommonResult.success(waterfallResult);
    }

    @Override
    public CommonResult<String> finish(FinishParam finishParam) {

        Integer authorId = 0;

        int i = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", finishParam.getPostId())
                        .eq("author_id", authorId)
                        .eq("is_finish", (byte) 0)
                        .set("is_finish", (byte) 1)
                        .set("updated_time", LocalDateTime.now())
        );

        //更新了is_finish字段，删除redis缓存
        redisUtil.remove("postId:" + finishParam.getPostId());

        if (i != 1) {
            log.error("数据库操作异常，帖子完成接口更新异常，更新数：{}", i);
            return CommonResult.fail("更新失败");
        }

        log.info("帖子作者结束组队，作者id：{}， 更改帖子id： {}， 更改时间： {}",
                authorId, finishParam.getPostId(), LocalDateTime.now());

        return CommonResult.success("更新成功");
    }

    @Override
    public CommonResult<DetailResult> detail(Integer postId) {

        //更新数据库里帖子的浏览量
        int update = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", postId)
                        .setSql("`watch_num` = `watch_num` + 1")
        );

        //redis里获取post
        Post post = (Post)redisUtil.get("postId:" + postId);

        if (post == null) {
            post = postMapper.selectOne(
                    new QueryWrapper<Post>()
                            .eq("id", postId)
                            .last("limit 1")
            );
            if (post == null) {
                log.warn("帖子详情页查询帖子失败， 查询不到， 查询帖子id: {}", postId);
                return CommonResult.fail("帖子不村子");
            }
            redisUtil.set("postId:" + postId, post);
        }

        //watch_num原子自增
        Long watchNum = redisUtil.incrWatchNum("postId:" + postId + "watch_num", post.getWatchNum().longValue());

        if (update != 1) {
            log.error("帖子详情页异常，更新帖子浏览数失败，浏览帖子id： {}", postId);
            return CommonResult.fail("浏览失败");
        }

        //redis获取用户信息
        GetUserInfoResult author = this.getAuthor(post.getAuthorId());

        if (author == null) {
            log.warn("帖子详情页异常，获取帖子作者失败，查询作者id：{}， 查询帖子id： {}", post.getAuthorId(), post.getId());
            return CommonResult.fail("操作异常");
        }

        //拼装数据
        DetailResult result = new DetailResult();

        result.setTitle(post.getTitle());
        result.setBrief(post.getBrief());
        result.setContent(post.getContent());
        result.setAuthorName(author.getUsername());
        result.setWatchNum(watchNum.intValue());
        result.setStopTime(post.getGoTime().format(Constant.dateTimeFormatter));
        result.setStartTime(post.getStartTime().format(Constant.dateTimeFormatter));
        result.setEndTime(post.getEndTime().format(Constant.dateTimeFormatter));
        result.setIsFinish(post.getIsFinish());

        return CommonResult.success(result);
    }

    @Override
    public CommonResult<WaterfallResult> author(Integer authorId, Long lastTime) {

        WaterfallResult result = new WaterfallResult();

        List<Waterfall> waterfalls = new ArrayList<>();

        //设置时间
        if (lastTime == null) {
            lastTime = LocalDateTime.now().toEpochSecond(Constant.zoneOffset);
        }

        //
        LocalDateTime nextTime = LocalDateTime.now();

        //redis获取用户信息
//        GetUserInfoResult author = this.getAuthor(authorId);
//
//        if (author == null) {
//            log.warn("作者帖子页异常，获取帖子作者失败，查询作者id： {}",authorId);
//            return CommonResult.fail("操作异常");
//        }

        //循环填充内容
        for (Post post : postMapper.selectList(
                new QueryWrapper<Post>()
                        .eq("author_id", authorId)
                        .le("created_time", LocalDateTime.ofEpochSecond(lastTime,0,Constant.zoneOffset))
                        .last("limit 10")
        )) {

            //组装帖子信息
            waterfalls.add(setWaterfall(post));

            nextTime = post.getCreatedTime();
        }

        result.setWaterfalls(waterfalls);
        result.setNextTime(nextTime.toEpochSecond(Constant.zoneOffset));

        return CommonResult.success(result);
    }

    @Override
    public CommonResult<String> delete(DeleteParam deleteParam) {

        /*
        *
        * 避免在每次调用 LocalDateTime.now() 时创建新对象，可以将其提前保存为一个变量。
        * 可以考虑将 is_deleted 列建立索引，以加快查询速度。
        * 如果你的业务场景中有多个同时执行的删除操作，可以将它们放在一个 Redis 队列中，使用一个单独的线程来处理这个队列，这样可以减轻数据库的负担，提高系统的性能。
        * 如果业务场景需要支持撤销删除操作，可以在一个单独的表中保存被删除的帖子，而不是直接将 is_deleted 标记为 1。这样可以避免误删的情况，并且在需要恢复数据时也会更加方便。
        *
        */


        int update = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", deleteParam.getPostId())
                        .eq("author_id", deleteParam.getAuthorId())
                        .eq("is_deleted", 0)
                        .set("updated_time", LocalDateTime.now())
                        .set("is_deleted", 1));
        if (update != 1) {
            log.error("删除帖子页错误，更新数异常，更新数：{}， 操作帖子id： {}， 操作人id： {}",
                    update, deleteParam.getPostId(), deleteParam.getAuthorId());
            return CommonResult.fail("删除失败");
        }

        log.info("删除帖子操作成功， 删除帖子id： {}， 操作人id： {}", deleteParam.getPostId(), deleteParam.getAuthorId());

        return CommonResult.success("删除成功");
    }

    @Override
    public CommonResult<String> delay(DelayParam delayParam) {

        int update = postMapper.update(new Post(),
                new UpdateWrapper<Post>()
                        .eq("id", delayParam.getPostId())
                        .set("updated_time", LocalDateTime.now())
                        .set("end_time", delayParam.getTime())
        );
        if (update != 1) {
            log.error("延迟帖子失败，数据库更新数异常，为： {}, 操作帖子id： {}， 延迟时间： {}" ,
                    update, delayParam.getPostId(), delayParam.getTime());
            return CommonResult.fail("操作失败");
        }

        log.info("延迟帖子成功， 操作帖子id： {}， 延迟时间： {}" , delayParam.getPostId(), delayParam.getTime());

        return CommonResult.success("延迟成功");
    }

    private GetUserInfoResult getAuthor(Integer authorId) {
        //redis获取用户信息
        GetUserInfoResult author = (GetUserInfoResult)redisUtil.get("userId:" + authorId);

        //如果redis中没有用户信息，使用openfeign调用user-server的服务获取用户详细信息
        if (author == null ) {
            CommonResult<GetUserInfoResult> serverUserInfo = userServer.getUserInfo(authorId);
            if (Objects.equals(serverUserInfo.getCode(), "A0400")) {
                log.info("查询用户无信息，查询用户id {}", authorId);
                return null;
            }
            author = serverUserInfo.getData();
            redisUtil.set("userId:" + authorId, author);
        }
        return author;
    }

    //拼装waterfall
    private Waterfall setWaterfall(Post post) {

        //获取category_name
        String categoryName = (String)redisUtil.get("categoryId:" + post.getCategoryId());
        if (categoryName == null) {
            Category category = categoryMapper.selectOne(new QueryWrapper<Category>()
                    .eq("id", post.getCategoryId())
                    .last("limit 1")
            );
            if (category == null) {
                log.warn("setWaterfall方法查询category异常，未查找到正确的category，查询categoryId为：{}", post.getCategoryId());
                return null;
            }
            categoryName = category.getName();
            redisUtil.set("categoryId:" + category.getId(), categoryName);
        }

        //获取category_next_name
        String categoryNextName = (String)redisUtil.get("categoryNextId:" + post.getCategoryNextId());
        if (categoryNextName == null) {
            CategoryNext categoryNext = categoryNextMapper.selectOne(new QueryWrapper<CategoryNext>()
                    .eq("id", post.getCategoryNextId())
                    .last("limit 1")
            );
            if (categoryNext == null) {
                log.warn("setWaterfall方法查询categoryNext异常，未查找到正确的categoryNext，查询categoryNextId为：{}",
                        post.getCategoryNextId());
                return null;
            }
            categoryNextName = categoryNext.getName();
            redisUtil.set("categoryNextId:" + categoryNext.getId(), categoryNextName);
        }


        Waterfall waterfall = new Waterfall();

        waterfall.setPostId(post.getId());
        waterfall.setTitle(post.getTitle());

        waterfall.setCategory(categoryName);
        waterfall.setCategoryNext(categoryNextName);

        waterfall.setGender(post.getGender());
        waterfall.setNumber(post.getNumber());

        waterfall.setGoTime(post.getGoTime().format(Constant.dateTimeFormatter));

        return  waterfall;
    }
}
