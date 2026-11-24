package com.cclinux.projects.meetroom.controller.cust;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.core.platform.controller.BaseController;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.exception.AppException;
import com.cclinux.projects.meetroom.service.cust.PublicService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Notes: 常用模块
 * @Author: cclinux0730 (weixin)
 * @Date:  2/18 7:32
 * @Ver: ccminicloud-framework 3.2.1
 */

@RestController("MeetRoomPublicController")
public class PublicController extends BaseController {

    @Resource(name = "MeetRoomPublicService")
    PublicService publicService;

    @RequestMapping("/comm/upload")
    @ResponseBody
    public ApiResult upload(
            @RequestParam(value = "file") MultipartFile file ) throws Exception {

        if (file.isEmpty()) throw new AppException("上传文件不能为空");
        if (file.getSize() > 5 * 1024 * 1024) throw new AppException("图片大小不能超过5MB");

        String fileName = file.getOriginalFilename();
        if (StrUtil.isEmpty(fileName) || !fileName.contains("."))
            throw new AppException("上传文件缺少扩展名");

        String extend = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        extend = extend.toLowerCase();
        if (!StrUtil.equalsAny(extend, ".png", ".jpg", ".jpeg"))
            throw new AppException("只能上传png,jpg格式的图片");

        // 魔数校验，防止伪造扩展名上传其他内容
        byte[] head = file.getBytes();
        boolean magicOk = head.length >= 8
                && ((head[0] == (byte) 0x89 && head[1] == 0x50 && head[2] == 0x4E && head[3] == 0x47)   // PNG
                || (head[0] == (byte) 0xFF && head[1] == (byte) 0xD8 && head[2] == (byte) 0xFF));       // JPEG
        if (!magicOk) throw new AppException("文件内容与图片格式不符");

        String url = publicService.upload(file);

        return ApiResult.success(MapUtil.of("fileID", url));
    }
}
