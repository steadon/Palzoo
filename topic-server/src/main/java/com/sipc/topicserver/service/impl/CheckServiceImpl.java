package com.sipc.topicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sipc.topicserver.mapper.PostMapper;
import com.sipc.topicserver.pojo.domain.Post;
import com.sipc.topicserver.pojo.dto.CommonResult;
import com.sipc.topicserver.pojo.dto.result.DetailNumResult;
import com.sipc.topicserver.pojo.dto.result.IsAuthorResult;
import com.sipc.topicserver.service.CheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/9 16:28
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    private PostMapper postMapper;

    @Override
    public CommonResult<DetailNumResult> detailNum(Integer postId) {

        Post post = postMapper.selectOne(new QueryWrapper<Post>().select("number").eq("id", postId).last("limit 1"));

        if (post == null) {
            return CommonResult.fail("查询失败");
        }

        DetailNumResult detailNumResult = new DetailNumResult();

        detailNumResult.setNum(post.getNumber());

        return CommonResult.success(detailNumResult);
    }

    @Override
    public CommonResult<IsAuthorResult> isAuthor(Integer userId, Integer postId) {

        Post post = postMapper.selectOne(
                new QueryWrapper<Post>()
                        .eq("id", postId)
                        .eq("author_id", userId)
                        .last("limit 1")
        );

        boolean isAuthor;

        if (post != null) {
            isAuthor = true;
        } else {
            isAuthor = false;
        }

        IsAuthorResult isAuthorResult = new IsAuthorResult();
        isAuthorResult.setIsAuthor(isAuthor);

        return CommonResult.success(isAuthorResult);
    }
}
