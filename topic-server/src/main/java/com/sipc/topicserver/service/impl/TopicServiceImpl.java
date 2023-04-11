package com.sipc.topicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sipc.topicserver.config.DirectRabbitConfig;
import com.sipc.topicserver.constant.Constant;
import com.sipc.topicserver.mapper.CategoryMapper;
import com.sipc.topicserver.mapper.PostMapper;
import com.sipc.topicserver.pojo.domain.Category;
import com.sipc.topicserver.pojo.domain.Post;
import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.mic.result.GetUserInfoResult;
import com.sipc.topicserver.pojo.dto.param.*;
import com.sipc.topicserver.pojo.dto.result.*;
import com.sipc.topicserver.pojo.dto.result.teamServer.GetTeamIdResult;
import com.sipc.topicserver.pojo.dto.result.teamServer.GetTeamInfoResult;
import com.sipc.topicserver.pojo.dto.resultEnum.ResultEnum;
import com.sipc.topicserver.service.TopicService;
import com.sipc.topicserver.service.openfeign.TeamServer;
import com.sipc.topicserver.service.openfeign.UserServer;
import com.sipc.topicserver.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    //找不到UserServer的been
    @Resource
    private UserServer userServer;

    @Resource
    private TeamServer teamServer;

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
//        List<Integer> categoryNextIds = new ArrayList<>();
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
                    redisUtil.set("categoryName:" + category, categoryId, (long)60, TimeUnit.MINUTES);
                }

                //输入筛选项
                screen.put("category_id", categoryId.toString());
            }

            //单独处理最少人数
            String numberMin = screen.get("numberMin");

            if (numberMin != null) {

                numberMinEq = Integer.parseInt(numberMin);
            }

            //单独处理最多人数
            String numberMax = screen.get("numberMax");

            if (numberMax != null) {

                numberMaxEq = Integer.parseInt(numberMax);
            }

            screen.remove("category");
            screen.remove("categoryNext");
            screen.remove("numberMin");
            screen.remove("numberMax");
            if (screen.get("gender") == null) {
                screen.remove("gender");
            }

        }

        if (searchParam.getStartTime() == null) {
            searchParam.setStartTime("1978-09-01 01:01:01");
        }

        if (searchParam.getEndTime() == null) {
            searchParam.setEndTime("2033-01-01 01:01:01");
        }

        if (searchParam.getLastTime() == null) {
            searchParam.setLastTime(LocalDateTime.now().toEpochSecond(Constant.zoneOffset));
        }

        //
        LocalDateTime nextTime = LocalDateTime.now();


        List<Post> postList;

        //如果没有子标签

        postList = postMapper.selectList(
                new QueryWrapper<Post>()
                        .allEq(screen)
                        .lt("created_time", LocalDateTime.ofEpochSecond(searchParam.getLastTime(),0,Constant.zoneOffset))
                        .ge("go_time", LocalDateTime.now())
                        .ge("go_time", LocalDateTime.parse(searchParam.getStartTime(),
                                Constant.dateTimeFormatter))
                        .lt("go_time", LocalDateTime.parse(searchParam.getEndTime(),
                                Constant.dateTimeFormatter))
                        .apply("((number >= " + numberMinEq +" AND number <= "+ numberMaxEq+") OR (number = " + 0+"))")
                        .orderByDesc("created_time")
                        .last("limit 10")
        );

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

//        Integer authorId = 0;

        int i;
        if (finishParam.getUserId() == 0) {
            i = postMapper.update(new Post(),
                    new UpdateWrapper<Post>()
                            .eq("id", finishParam.getPostId())
                            .eq("is_finish", (byte) 0)
                            .set("is_finish", (byte) 1)
                            .set("updated_time", LocalDateTime.now())
            );
        } else {
            i = postMapper.update(new Post(),
                    new UpdateWrapper<Post>()
                            .eq("id", finishParam.getPostId())
                            .eq("author_id", finishParam.getUserId())
                            .eq("is_finish", (byte) 0)
                            .set("is_finish", (byte) 1)
                            .set("updated_time", LocalDateTime.now())
            );
        }

        //更新了is_finish字段，删除redis缓存
        redisUtil.remove("postId:" + finishParam.getPostId());

        if (i != 1) {
            log.error("数据库操作异常，帖子完成接口更新异常，更新数：{}", i);
            return CommonResult.fail("更新失败");
        }

        log.info("帖子作者结束组队，作者id：{}， 更改帖子id： {}， 更改时间： {}",
                finishParam.getUserId(), finishParam.getPostId(), LocalDateTime.now());

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
        if (update != 1) {
            log.warn("帖子详细信息接口异常，更新观看人数异常，更新帖子id： {}， 更新数： {}", postId, update);
            return CommonResult.fail("浏览请求失败");
        }

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
            redisUtil.set("postId:" + postId, post, (long)60, TimeUnit.MINUTES);
        }

        //watch_num原子自增
        Long watchNum = redisUtil.incrWatchNum("postId:" + postId + "watch_num", post.getWatchNum().longValue());

        //redis获取用户信息
        GetUserInfoResult author = this.getAuthor(post.getAuthorId());

        if (author == null) {
            log.warn("帖子详情页异常，获取帖子作者失败，查询作者id：{}， 查询帖子id： {}", post.getAuthorId(), post.getId());
            return CommonResult.fail("操作异常");
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setName(author.getUsername());
        userInfo.setGender(author.getGender());
        userInfo.setYear(20);
        userInfo.setSchool("天津理工大学");

        //拼装数据
        DetailResult result = new DetailResult();

        result.setTitle(post.getTitle());
        result.setContent(post.getContent());
        result.setAuthor(userInfo);

        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().eq("id", post.getCategoryId()).last("limit 1"));
        if (category == null) {

            return CommonResult.fail("查询失败");
        }
        result.setCategory(category.getName());

        if (post.getCategoryNext() != null) {
            List<String> categoryNext = new ArrayList<>(Arrays.asList(post.getCategoryNext().split("\\+")));
            result.setCategoryNext(categoryNext);
        } else {
            result.setCategoryNext(null);
        }

        if (post.getGender() == 0) {
            result.setGender("性别不限");
        } else if (post.getGender() == 1) {
            result.setGender("男");
        } else  if (post.getGender() == 2) {
            result.setGender("女");
        }

        result.setWatchNum(watchNum.intValue());
        result.setNum(post.getNumber());
        result.setGoTime(post.getGoTime().format(Constant.dateTimeFormatter));
        result.setIsFinish(post.getIsFinish());
        result.setNowNum(getNowNum(postId));

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
                        .lt("created_time", LocalDateTime.ofEpochSecond(lastTime,0,Constant.zoneOffset))
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
        int update ;
        if (deleteParam.getUserId() == 0) {
            update = postMapper.update(new Post(),
                    new UpdateWrapper<Post>()
                            .eq("id", deleteParam.getPostId())
                            .eq("is_deleted", 0)
                            .set("updated_time", LocalDateTime.now())
                            .set("is_deleted", 1));
        } else {
            update = postMapper.update(new Post(),
                    new UpdateWrapper<Post>()
                            .eq("id", deleteParam.getPostId())
                            .eq("author_id", deleteParam.getUserId())
                            .eq("is_deleted", 0)
                            .set("updated_time", LocalDateTime.now())
                            .set("is_deleted", 1));
        }

        if (update != 1) {
            log.error("删除帖子页错误，更新数异常，更新数：{}， 操作帖子id： {}， 操作人id： {}",
                    update, deleteParam.getPostId(), deleteParam.getUserId());
            return CommonResult.fail("删除失败");
        }

        //更新了is_deleted字段，删除redis缓存
        redisUtil.remove("postId:" + deleteParam.getPostId());

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
            redisUtil.set("userId:" + authorId, author, (long)60, TimeUnit.MINUTES);
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
            redisUtil.set("categoryId:" + category.getId(), categoryName, (long)60, TimeUnit.MINUTES);
        }

        //获取category_next_name
        Waterfall waterfall = new Waterfall();

        waterfall.setPostId(post.getId());
        waterfall.setTitle(post.getTitle());

        waterfall.setCategory(categoryName);

        if (post.getCategoryNext() != null) {
            List<String> categoryNext = new ArrayList<>(Arrays.asList(post.getCategoryNext().split("\\+")));
            waterfall.setCategoryNext(categoryNext);
        } else {
            waterfall.setCategoryNext(null);
        }

        if (post.getGender() == 0) {
            waterfall.setGender("性别不限");
        } else if (post.getGender() == 1) {
            waterfall.setGender("男");
        } else  if (post.getGender() == 2) {
            waterfall.setGender("女");
        }

        waterfall.setNumber(post.getNumber());

        waterfall.setGoTime(post.getGoTime().format(Constant.getDateTimeFormatterResult));

        return  waterfall;
    }

    private Integer getNowNum(Integer postId) {
        CommonResult<GetTeamIdResult> teamIdResult = teamServer.getTeamId(postId);
        if (!Objects.equals(teamIdResult.getCode(), ResultEnum.SUCCESS.getCode())) {
            return null;
        }
        if (teamIdResult.getData() != null && teamIdResult.getData().getTeamId() != null) {
            CommonResult<GetTeamInfoResult> teamInfoResult = teamServer.getTeamInfo(teamIdResult.getData().getTeamId());
            if (!Objects.equals(teamInfoResult.getCode(), ResultEnum.SUCCESS.getCode())) {
                return null;
            }
            if (teamInfoResult.getData() != null && teamInfoResult.getData().getActNum() != null) {
                return teamInfoResult.getData().getActNum();
            }
        }
        return null;
    }


}
