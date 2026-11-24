package com.cclinux.projects.meetroom.service.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.projects.meetroom.comm.PwdHelper;
import com.cclinux.projects.meetroom.mapper.AdminMapper;
import com.cclinux.projects.meetroom.model.AdminModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Notes: 后台管理员模块
 * @Author: cclinux0730 (weixin)
 * @Date:  2/16 9:35
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("MeetRoomAdminMgrService")
public class AdminMgrService extends BaseMyAdminService {
    @Resource(name = "MeetRoomAdminMapper")
    private AdminMapper adminMapper;


    /**
     * 管理员列表
     */
    public PageResult getMgrList(Long adminId, PageParams pageRequest) {
        Where<AdminModel> whereAdmin = new Where<>();
        whereAdmin.eq("ADMIN_ID", adminId);
        whereAdmin.eq("ADMIN_STATUS", AdminModel.STATUS.NORMAL);
        whereAdmin.in("ADMIN_TYPE", AdminModel.TYPE.COMM, AdminModel.TYPE.SUPER);
        AdminModel admin = adminMapper.getOne(whereAdmin);
        if (ObjectUtil.isEmpty(admin)) this.appError("管理员不存在");

        Where<AdminModel> where = new Where<>();

        if (admin.getAdminType() == AdminModel.TYPE.COMM) { //普通管理员只能管理其他管理员
            where.eq("ADMIN_TYPE", AdminModel.TYPE.OTHTER);
        }

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(
                    wrapper -> {
                        wrapper.or().like("ADMIN_NAME", search);
                        wrapper.or().like("ADMIN_PHONE", search);
                        wrapper.or().like("ADMIN_DESC", search);
                    }
            );
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "status": {
                    where.eq("ADMIN_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "type": {
                    where.eq("ADMIN_TYPE", Convert.toInt(sortVal));
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("ADMIN_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return adminMapper.getPageList(page, where, "ADMIN_ID,ADMIN_NAME,ADMIN_STATUS,ADMIN_PHONE,ADMIN_TYPE," +
                "ADMIN_LOGIN_CNT,ADMIN_LOGIN_TIME,ADMIN_DESC,ADD_TIME");
    }

    /**
     * 删除管理员
     */
    public void delMgr(Long id, Long myAdminId) {
        if (NumberUtil.equals(id, myAdminId)) this.appError("不能删除自己");
        AdminModel admin = adminMapper.getOne(id);
        if (ObjectUtil.isEmpty(admin)) this.appError("管理员不存在");
        if (admin.getAdminType() == AdminModel.TYPE.SUPER) this.appError("超级管理员不可删除");

        adminMapper.delete(id);
    }

    /**
     * 添加管理员
     */
    public void insertMgr(AdminModel admin, long myAdminId) {
        Where<AdminModel> where = new Where<>();
        where.eq("ADMIN_NAME", admin.getAdminName());
        if (adminMapper.exists(where)) this.appError("该账号已经存在");

        AdminModel me = adminMapper.getOne(myAdminId);
        if (ObjectUtil.isEmpty(me)) this.appError("管理员不存在");
        if (me.getAdminType() != AdminModel.TYPE.SUPER && admin.getAdminType() != AdminModel.TYPE.OTHTER)
            this.appError("仅超级管理员可添加管理员账号");

        admin.setAdminId(null);
        admin.setAdminStatus(AdminModel.STATUS.NORMAL);
        admin.setAdminPassword(PwdHelper.hash(admin.getAdminPassword()));
        adminMapper.add(admin);
    }

    /**
     * 修改管理员
     */
    public void editMgr(AdminModel admin, long myAdminId) {
        AdminModel old = adminMapper.getOne(admin.getAdminId());
        if (ObjectUtil.isEmpty(old)) this.appError("管理员不存在");
        if (old.getAdminType() == AdminModel.TYPE.SUPER) this.appError("超级管理员不可修改");

        AdminModel me = adminMapper.getOne(myAdminId);
        if (ObjectUtil.isEmpty(me)) this.appError("管理员不存在");
        if (me.getAdminType() != AdminModel.TYPE.SUPER && old.getAdminType() != AdminModel.TYPE.OTHTER)
            this.appError("仅超级管理员可修改管理员账号");

        // 账号不可重复
        Where<AdminModel> whereName = new Where<>();
        whereName.eq("ADMIN_NAME", admin.getAdminName());
        whereName.ne("ADMIN_ID", admin.getAdminId());
        if (adminMapper.exists(whereName)) this.appError("该账号已经存在");

        UpdateWhere<AdminModel> uw = new UpdateWhere<>();
        uw.eq("ADMIN_ID", admin.getAdminId());
        uw.set("ADMIN_NAME", admin.getAdminName());
        uw.set("ADMIN_DESC", admin.getAdminDesc());
        uw.set("ADMIN_PHONE", admin.getAdminPhone());
        if (StrUtil.isNotEmpty(admin.getAdminPassword()))
            uw.set("ADMIN_PASSWORD", PwdHelper.hash(admin.getAdminPassword()));
        // 仅超级管理员可变更类型，防止权限提升
        if (me.getAdminType() == AdminModel.TYPE.SUPER)
            uw.set("ADMIN_TYPE", admin.getAdminType());
        adminMapper.edit(uw);
    }

    /**
     * 获取管理员信息
     */
    public Map<String, Object> getMgrDetail(long id) {
        return adminMapper.getOneMap(id, "ADMIN_ID,ADMIN_TYPE,ADMIN_STATUS,ADMIN_NAME,ADMIN_DESC,ADMIN_PHONE");
    }

    /**
     * 修改管理员自身密码
     */
    public void pwdMgr(long adminId, String oldPassword, String password) {
        AdminModel admin = adminMapper.getOne(adminId);
        if (ObjectUtil.isEmpty(admin)) this.appError("管理员不存在");

        if (!PwdHelper.verify(oldPassword, admin.getAdminPassword()))
            this.appError("旧密码错误，请重新输入");

        UpdateWhere<AdminModel> uw = new UpdateWhere<>();
        uw.eq("ADMIN_ID", adminId);
        uw.set("ADMIN_PASSWORD", PwdHelper.hash(password));
        adminMapper.edit(uw);
    }

    /**
     * 修改状态
     */
    public void statusMgr(long id, int status, long myAdminId) {
        if (NumberUtil.equals(id, myAdminId)) this.appError("不能修改自己的状态");
        AdminModel admin = adminMapper.getOne(id);
        if (ObjectUtil.isEmpty(admin)) this.appError("管理员不存在");
        if (admin.getAdminType() == AdminModel.TYPE.SUPER) this.appError("超级管理员状态不可修改");

        UpdateWhere<AdminModel> uw = new UpdateWhere<>();
        uw.eq("ADMIN_ID", id);
        uw.set("ADMIN_STATUS", status);
        adminMapper.edit(uw);
    }
}
