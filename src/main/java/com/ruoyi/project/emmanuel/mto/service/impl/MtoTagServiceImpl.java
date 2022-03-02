package com.ruoyi.project.emmanuel.mto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ToolUtils;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.emmanuel.mto.domain.MtoTag;
import com.ruoyi.project.emmanuel.mto.mapper.MtoTagMapper;
import com.ruoyi.project.emmanuel.mto.service.IMtoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.text.Convert;

/**
 * mtoService业务层处理
 *
 * @author 一粒麦子
 * @date 2021-11-27
 */
@Service
public class MtoTagServiceImpl extends ServiceImpl<MtoTagMapper, MtoTag> implements IMtoTagService {
    @Autowired
    private MtoTagMapper mtoTagMapper;

    /**
     * 查询mto
     *
     * @param id mto主键
     * @return mto
     */
    @Override
    public MtoTag selectMtoTagById(Long id) {
        return mtoTagMapper.selectById(id);
    }

    /**
     * 查询mto列表
     *
     * @param mtoTag mto
     * @return mto
     */
    @Override
    public TableDataInfo selectMtoTagList(MtoTag mtoTag) {
        // 分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        IPage<MtoTag> page = null;
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            page = new Page<>(pageNum, pageSize);
        }
        IPage<MtoTag> tagIPage = mtoTagMapper.selectPage(page, null);

        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setTotal(tagIPage.getTotal());
        tableDataInfo.setRows(tagIPage.getRecords());

        return tableDataInfo;
    }

    /**
     * 新增mto
     *
     * @param mtoTag mto
     * @return 结果
     */
    @Override
    public int insertMtoTag(MtoTag mtoTag) {
        mtoTag.setCreateTime(DateUtils.getNowDate());
       return mtoTagMapper.insert(mtoTag);
    }

    /**
     * 修改mto
     *
     * @param mtoTag mto
     * @return 结果
     */
    @Override
    public int updateMtoTag(MtoTag mtoTag) {
        mtoTag.setUpdateTime(DateUtils.getNowDate());
        return mtoTagMapper.updateById(mtoTag);
    }

    /**
     * 批量删除mto
     *
     * @param ids 需要删除的mto主键
     * @return 结果
     */
    @Override
    public int deleteMtoTagByIds(String ids) {
        if (ToolUtils.isEmpty(ids)){
            throw new RuntimeException("至少选择一个删除");
        }
        ArrayList<String> idList = new ArrayList<>(Arrays.asList(ids.split(",")));
        return mtoTagMapper.deleteBatchIds(idList);
    }

    /**
     * 删除mto信息
     *
     * @param id mto主键
     * @return 结果
     */
    @Override
    public int deleteMtoTagById(Long id) {
        return mtoTagMapper.deleteById(id);
    }
}
