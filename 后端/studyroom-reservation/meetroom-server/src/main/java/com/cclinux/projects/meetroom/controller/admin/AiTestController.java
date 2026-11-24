package com.cclinux.projects.meetroom.controller.admin;

import cn.hutool.core.map.MapUtil;
import com.cclinux.framework.constants.AppCode;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meetroom.service.admin.AiTestService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Notes: AI 测试中心控制器（生成测试用例 / 执行测试用例 / 内置回归套件）
 */
@RestController("MeetRoomAiTestController")
public class AiTestController extends BaseMyAdminController {

    @Resource(name = "MeetRoomAiTestService")
    private AiTestService aiTestService;

    private void checkAdmin() {
        if (request.getAttribute("adminId") == null)
            throw new AppException("登录已失效，请重新登录", AppCode.ERROR_TOKEN);
    }

    @RequestMapping(value = "/admin/aitest/generate")
    public ApiResult generate(@RequestBody Map<String, Object> input) {
        checkAdmin();

        // 数据校验
        String[] RULES = {
                "requirement:must|string|min=1|max=2000|name=需求描述",
        };
        DataCheck.check(input, RULES);

        String requirement = MapUtil.getStr(input, "requirement");

        List<Map<String, Object>> cases = aiTestService.generate(requirement);

        return ApiResult.success(MapUtil.of("cases", cases));
    }

    @RequestMapping(value = "/admin/aitest/run")
    public ApiResult run(@RequestBody Map<String, Object> input) {
        checkAdmin();

        List<Map<String, Object>> cases = null;
        Object casesObj = input.get("cases");
        if (casesObj instanceof List) {
            //noinspection unchecked
            cases = (List<Map<String, Object>>) casesObj;
        }

        Map<String, Object> ret = aiTestService.run(cases);

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/aitest/regression")
    public ApiResult regression() {
        checkAdmin();

        List<Map<String, Object>> cases = aiTestService.regression();

        return ApiResult.success(MapUtil.of("cases", cases));
    }

}
