package com.cclinux.projects.meetroom.service.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
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
import com.cclinux.projects.meetroom.comm.PwdHelper;
import com.cclinux.projects.meetroom.mapper.UserMapper;
import com.cclinux.projects.meetroom.model.UserModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Notes: 用户管理业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date:  12/15 12:10
 * @Ver: ccminicloud-framework 3.2.1
 */


@Service("MeetRoomAdminUserService")
public class AdminUserService extends BaseMyAdminService {


    @Resource(name = "MeetRoomUserMapper")
    private UserMapper userMapper;


    /**
     * 用户列表
     */
    public PageResult getAdminUserList(PageParams pageRequest) {

        Where<UserModel> where = new Where<>();

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("USER_NAME", search);
                wrapper.or().like("USER_ACCOUNT", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "status": {
                    where.eq("USER_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("USER_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return userMapper.getPageList(page, where, "*");
    }

    /**
     * 删除用户
     */
    public void delUser(long id) {
        userMapper.delete(id);
    }


    /**
     * 获取单个用户
     */
    public Map<String, Object> getUserDetail(long id) {
        return userMapper.getOneMap(id);
    }

    /**
     * 修改用户状态
     */
    public void statusUser(long id, int status) {
        UpdateWhere<UserModel> uw = new UpdateWhere<>();
        uw.eq("USER_ID", id);
        uw.set("USER_STATUS", status);
        userMapper.edit(uw);
    }


    /**
     * 重置用户密码
     */
    public void editUserPwd(long id, String pwd) {
        UpdateWhere<UserModel> uw = new UpdateWhere<>();
        uw.eq("USER_ID", id);
        uw.set("USER_PASSWORD", PwdHelper.hash(pwd));
        userMapper.edit(uw);
    }


    /**
     * 导出用户名单 Excel
     */
    public Map<String, Object> exportUserJoinExcel(String fmt) {
        Where<UserModel> where = new Where<>();
        where.orderByDesc("USER_ID");

        List<UserModel> list = userMapper.getAllList(where, "");

        List<Map<String, Object>> rows = new ArrayList<>();
        for (UserModel user : list) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("用户ID", user.getUserId());
            row.put("账号", user.getUserAccount());
            row.put("姓名", user.getUserName());
            row.put("状态", user.getUserStatus() == UserModel.STATUS.NORMAL ? "正常" : "禁用");
            row.put("登录次数", user.getUserLoginCnt());
            row.put("最后登录", user.getUserLoginTime() == 0 ? "" : TimeHelper.timestamp2Time(user.getUserLoginTime()));
            rows.add(row);
        }

        String filePath = FileHelper.getFileSavePath(ProjectConfig.PID, "xlsx", "export-user");
        FileUtil.mkParentDirs(filePath);
        ExcelWriter writer = ExcelUtil.getWriter(filePath);
        writer.write(rows, true);
        writer.close();

        Map<String, Object> ret = new HashMap<>();
        ret.put("url", FileHelper.getFileSaveURL(filePath));
        return ret;
    }


}
