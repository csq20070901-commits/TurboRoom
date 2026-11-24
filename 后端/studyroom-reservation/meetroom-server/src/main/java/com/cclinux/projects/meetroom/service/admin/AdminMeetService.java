package com.cclinux.projects.meetroom.service.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.helper.FileHelper;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.meetroom.comm.ProjectConfig;
import com.cclinux.projects.meetroom.mapper.MeetJoinMapper;
import com.cclinux.projects.meetroom.mapper.MeetMapper;
import com.cclinux.projects.meetroom.model.MeetJoinModel;
import com.cclinux.projects.meetroom.model.MeetModel;
import com.cclinux.projects.meetroom.service.cust.MeetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service("MeetRoomAdminMeetService")
public class AdminMeetService extends BaseMyAdminService {


    @Resource(name = "MeetRoomMeetMapper")
    private MeetMapper meetMapper;

    @Resource(name = "MeetRoomMeetJoinMapper")
    private MeetJoinMapper meetJoinMapper;

    @Resource(name = "MeetRoomMeetService")
    private MeetService meetService;


    /**
     * 添加场地
     */
    public long insertMeet(MeetModel meet) {
        meet.setMeetId(null);
        meet.setMeetStatus(MeetModel.STATUS.NORMAL);
        meet.setMeetVouch(0);
        meet.setMeetViewCnt(0);
        if (StrUtil.isEmpty(meet.getMeetDays())) meet.setMeetDays("[]");

        meetMapper.add(meet);
        return meet.getMeetId();
    }

    /**
     * 修改场地
     */
    public void editMeet(MeetModel meet) {
        if (ObjectUtil.isNull(meet.getMeetId()) || meet.getMeetId() <= 0)
            appError("场地不存在");

        MeetModel old = meetMapper.getOne(meet.getMeetId());
        if (ObjectUtil.isNull(old)) appError("场地不存在");

        UpdateWhere<MeetModel> uw = new UpdateWhere<>();
        uw.eq("MEET_ID", meet.getMeetId());
        uw.set("MEET_TITLE", meet.getMeetTitle());
        uw.set("MEET_CATE_ID", meet.getMeetCateId());
        uw.set("MEET_CATE_NAME", meet.getMeetCateName());
        uw.set("MEET_ORDER", meet.getMeetOrder());
        uw.set("MEET_MAX_CNT", meet.getMeetMaxCnt());
        uw.set("MEET_FORMS", meet.getMeetForms());
        uw.set("MEET_OBJ", meet.getMeetObj());
        meetMapper.edit(uw);
    }

    /**
     * 设置可预约日期
     */
    public void setMeetDays(long meetId, String days) {
        try {
            JSONUtil.parseArray(days);
        } catch (Exception e) {
            appError("日期数据格式不正确");
        }

        UpdateWhere<MeetModel> uw = new UpdateWhere<>();
        uw.eq("MEET_ID", meetId);
        uw.set("MEET_DAYS", days);
        meetMapper.edit(uw);
    }


    /**
     * 场地列表
     */
    public PageResult getAdminMeetList(PageParams pageRequest) {

        Where<MeetModel> where = new Where<>();

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("MEET_TITLE", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "cateId": {
                    where.eq("MEET_CATE_ID", Convert.toLong(sortVal));
                    break;
                }
                case "status": {
                    where.eq("MEET_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "vouch": {
                    where.eq("MEET_VOUCH", 1);
                    break;
                }
                case "top": {
                    where.eq("MEET_ORDER", 0);
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByAsc("MEET_ORDER");
        where.orderByDesc("MEET_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return meetMapper.getPageList(page, where, "*");
    }

    /**
     * 预约名单
     */
    public PageResult getAdminMeetJoinList(PageParams pageRequest) {

        Where<MeetJoinModel> where = new Where<>();


        long meetId = pageRequest.getParamLong("meetId");
        where.eq("MEET_JOIN_MEET_ID", meetId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            if (search.length() == 21 && search.contains("#")) {
                // 日期查询
                String[] arr = search.split("#");
                where.between("MEET_JOIN_DAY", arr[0], arr[1]);
            } else {
                where.and(wrapper -> {
                    wrapper.or().like("MEET_JOIN_CODE", search);
                    wrapper.or().like("MEET_JOIN_OBJ", search);
                });
            }

        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "check": {
                    where.eq("MEET_JOIN_IS_CHECK", Convert.toInt(sortVal));
                    break;
                }
                case "status": {
                    where.eq("MEET_JOIN_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("MEET_JOIN_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return meetJoinMapper.getPageList(page, where, "*");
    }

    /**
     * 删除场地（连同其预约记录）
     */
    @Transactional
    public void delMeet(long id) {
        meetMapper.delete(id);

        Where<MeetJoinModel> whereJoin = new Where<>();
        whereJoin.eq("MEET_JOIN_MEET_ID", id);
        meetJoinMapper.delete(whereJoin);
    }

    /**
     * 删除预约记录
     */
    public void delMeetJoin(long meetJoinId) {
        meetJoinMapper.delete(meetJoinId);
    }


    /**
     * 获取单个场地
     */
    public Map<String, Object> getMeetDetail(long id) {
        return meetMapper.getOneMap(id);
    }

    /**
     * 修改场地状态
     */
    public void statusMeet(long id, int status) {
        UpdateWhere<MeetModel> uw = new UpdateWhere<>();
        uw.eq("MEET_ID", id);
        uw.set("MEET_STATUS", status);
        meetMapper.edit(uw);
    }


    /**
     * 管理员按钮核销（flag=1 核销，flag=0 撤销核销）
     */
    public void checkinMeetJoin(long meetJoinId, int flag) {
        MeetJoinModel meetJoin = meetJoinMapper.getOne(meetJoinId);
        if (ObjectUtil.isNull(meetJoin)) appError("预约记录不存在");

        UpdateWhere<MeetJoinModel> uw = new UpdateWhere<>();
        uw.eq("MEET_JOIN_ID", meetJoinId);
        uw.set("MEET_JOIN_IS_CHECK", flag);
        uw.set("MEET_JOIN_CHECK_TIME", flag == 1 ? TimeHelper.timestamp() : 0);
        meetJoinMapper.edit(uw);
    }

    /**
     * 修改排序
     */
    public void orderMeet(long id, int order) {
        UpdateWhere<MeetModel> uw = new UpdateWhere<>();
        uw.eq("MEET_ID", id);
        uw.set("MEET_ORDER", order);
        meetMapper.edit(uw);
    }


    /**
     * 首页推荐设定
     */
    public void vouchMeet(long id, int vouch) {
        UpdateWhere<MeetModel> uw = new UpdateWhere<>();
        uw.eq("MEET_ID", id);
        uw.set("MEET_VOUCH", vouch);
        meetMapper.edit(uw);
    }

    /**
     * 导出预约名单 Excel
     */
    public Map<String, Object> exportMeetJoinExcel(long meetId, String start, String end) {
        Where<MeetJoinModel> where = new Where<>();
        where.eq("MEET_JOIN_MEET_ID", meetId);
        where.between("MEET_JOIN_DAY", start, end);
        where.orderByAsc("MEET_JOIN_ID");

        List<MeetJoinModel> list = meetJoinMapper.getAllList(where, "");

        List<Map<String, Object>> rows = new ArrayList<>();
        for (MeetJoinModel join : list) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("预约号", join.getMeetJoinCode());
            row.put("姓名", getFormVal(join.getMeetJoinForms(), "name"));
            row.put("手机", getFormVal(join.getMeetJoinForms(), "phone"));
            row.put("预约日期", join.getMeetJoinDay());
            row.put("预约时段", join.getMeetJoinTime());
            row.put("核销状态", join.getMeetJoinIsCheck() == 1 ? "已核销" : "待核销");
            rows.add(row);
        }

        String filePath = FileHelper.getFileSavePath(ProjectConfig.PID, "xlsx", "export");
        FileUtil.mkParentDirs(filePath);
        ExcelWriter writer = ExcelUtil.getWriter(filePath);
        writer.write(rows, true);
        writer.close();

        Map<String, Object> ret = new HashMap<>();
        ret.put("url", FileHelper.getFileSaveURL(filePath));
        return ret;
    }

    /**
     * 清空该场地全部预约
     */
    public void clearMeetAll(long meetId) {
        Where<MeetJoinModel> where = new Where<>();
        where.eq("MEET_JOIN_MEET_ID", meetId);
        meetJoinMapper.delete(where);
    }

    /**
     * 管理员扫码核销
     */
    public void scanMeetJoin(long meetId, String code) {
        Where<MeetJoinModel> where = new Where<>();
        where.eq("MEET_JOIN_MEET_ID", meetId);
        where.eq("MEET_JOIN_CODE", code);
        MeetJoinModel join = meetJoinMapper.getOne(where);
        if (ObjectUtil.isNull(join)) appError("核销码无效或不属于该场地");

        if (join.getMeetJoinStatus() != MeetJoinModel.STATUS.NORMAL) appError("该预约已取消");
        if (join.getMeetJoinIsCheck() == 1) appError("该预约已核销，请勿重复操作");

        UpdateWhere<MeetJoinModel> uw = new UpdateWhere<>();
        uw.eq("MEET_JOIN_ID", join.getMeetJoinId());
        uw.set("MEET_JOIN_IS_CHECK", 1);
        uw.set("MEET_JOIN_CHECK_TIME", TimeHelper.timestamp());
        meetJoinMapper.edit(uw);
    }


    /**
     * 从预约表单中取字段值
     */
    private String getFormVal(String forms, String mark) {
        if (StrUtil.isEmpty(forms)) return "";
        try {
            JSONArray arr = JSONUtil.parseArray(forms);
            for (Object o : arr) {
                JSONObject f = (JSONObject) o;
                if (mark.equals(f.getStr("mark"))) return f.getStr("val");
            }
        } catch (Exception ignored) {
        }
        return "";
    }

}
