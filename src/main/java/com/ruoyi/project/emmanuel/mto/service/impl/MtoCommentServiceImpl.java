package com.ruoyi.project.emmanuel.mto.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.common.utils.ToolUtils;
import com.ruoyi.project.emmanuel.mto.domain.MtoComment;
import com.ruoyi.project.emmanuel.mto.mapper.MtoCommentMapper;
import com.ruoyi.project.emmanuel.mto.service.IMtoCommentService;
import com.ruoyi.project.emmanuel.mto.service.IWebPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MtoCommentServiceImpl extends ServiceImpl<MtoCommentMapper, MtoComment> implements IMtoCommentService {

    @Autowired
    private MtoCommentMapper commentMapper;

    @Autowired
    private IWebPostService postService;

    @Override
    public void messageBoard(ModelMap modelMap) {
        // 获取导航
        postService.selectCategory(modelMap);
        // 获取侧边栏
        postService.publicWeb(modelMap);
    }

    /**
     * 留言板新增留言，博客新增评论
     *
     * @param comment
     * @param request
     * @return
     */
    @Override
    public int insert(MtoComment comment, HttpServletRequest request) {
        if (comment.getPId() == null ||Objects.equals(0L, comment.getPId())) {
            comment.setAncestors("0");
            comment.setPId(0L);
        } else {
            // 查询上级信息
            MtoComment pInfo = commentMapper.selectById(comment.getPId());
            comment.setAncestors(pInfo.getAncestors() + "," + comment.getPId());
            comment.setParentNickName(pInfo.getNickName());
        }
        comment.setStatus("1");
        String ipAddr = IpUtils.getIpAddr(request);
        comment.setIp(ToolUtils.isEmpty(IpUtils.inetAton(ipAddr))?null:String.valueOf(IpUtils.inetAton(ipAddr)));
        comment.setCreateTime(DateUtils.getNowDate());
        return commentMapper.insert(comment);
    }

    /**
     * 查询留言
     *
     * @return
     */
    @Override
    public List<MtoComment> selectCommentList() {
        // 只查询一级
        QueryWrapper<MtoComment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MtoComment::getPId, 0L).eq(MtoComment::getPostId, 0L)
        .orderByDesc(MtoComment::getCreateTime);
        List<MtoComment> allComment = commentMapper.selectList(wrapper);

        allComment.stream().forEach(e -> {
                    e.setReplyComments(commentMapper.selectAllLowerLevel(e.getId()));
                }
        );
        return allComment;
    }

    /**
     * 查看博客评论
     * @param comment
     * @return
     */
    @Override
    public List<MtoComment> getBlogMessage(MtoComment comment) {
        Long postId = comment.getPostId();
        List<MtoComment> commentList = new ArrayList<>();
        if (null != postId ){
            commentList = commentMapper.selectByPostId(postId);
            commentList.stream().forEach(e->{
                e.setReplyComments(commentMapper.selectByPid(e.getId()));
            });
        }
        return commentList;
    }
}
