package com.cclinux.projects.meetroom.controller.cust;

import cn.hutool.core.map.MapUtil;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.projects.meetroom.service.cust.AiAssistantService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

@RestController("MeetRoomAiAssistantController")
public class AiAssistantController extends BaseMyCustController {
    @Resource(name = "MeetRoomAiAssistantService") private AiAssistantService aiAssistantService;

    /** 悬浮问答（未登录）按 IP 限流：窗口 10 分钟，最多 30 次，防止匿名消耗 AI 配额 */
    private static final long SUSPENDED_WINDOW_MS = 10 * 60 * 1000L;
    private static final int SUSPENDED_MAX_REQ = 30;
    private static final ConcurrentHashMap<String, Deque<Long>> SUSPENDED_HITS = new ConcurrentHashMap<>();

    @RequestMapping(value = "/ai/chat")
    public ApiResult chat(@RequestBody Map<String, Object> input) {
        return ApiResult.success(MapUtil.of("answer", aiAssistantService.chat(
                MapUtil.getStr(input, "question"), MapUtil.getStr(input, "scene"), history(input.get("history")))));
    }

    // AI 服务状态（前端"服务已就绪"指示灯数据源，不返回任何配置值）
    @LoginIgnore
    @RequestMapping(value = "/ai/status")
    public ApiResult status() {
        return ApiResult.success(MapUtil.of("enabled", aiAssistantService.aiEnabled()));
    }

    private List<Map<String, String>> history(Object raw) {
        List<Map<String, String>> result = new ArrayList<>();
        if (!(raw instanceof List<?>)) return result;
        for (Object entry : (List<?>) raw) {
            if (!(entry instanceof Map<?, ?>)) continue;
            Map<?, ?> item = (Map<?, ?>) entry;
            Map<String, String> message = new HashMap<>();
            Object role = item.get("role"), content = item.get("content");
            if (role instanceof String) message.put("role", (String) role);
            if (content instanceof String) message.put("content", (String) content);
            result.add(message);
        }
        return result;
    }

    // 悬浮问答对未登录访客开放，通过 IP 限流防滥用
    @LoginIgnore
    @RequestMapping(value = "/ai/suspended-chat")
    public Map<String, Object> suspendedChat(@RequestBody Map<String, Object> input, HttpServletRequest request) {
        String ip = clientIp(request);
        if (!allowSuspended(ip)) throw new com.cclinux.framework.exception.AppException("提问过于频繁，请稍后再试");

        String question = MapUtil.getStr(input, "question");
        String answer = aiAssistantService.chat(question, MapUtil.getStr(input, "scene"), history(input.get("history")));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("result", MapUtil.of("answer", answer));
        return result;
    }

    @RequestMapping(value = "/ai/recommend")
    public ApiResult recommend(@RequestBody Map<String, Object> input) {
        return ApiResult.success(aiAssistantService.recommend(
                MapUtil.getStr(input, "day"), MapUtil.getStr(input, "time"), MapUtil.getInt(input, "people", 0),
                MapUtil.getStr(input, "category"), MapUtil.getStr(input, "requirement")));
    }

    private String clientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            int idx = ip.indexOf(',');
            return (idx > 0 ? ip.substring(0, idx) : ip).trim();
        }
        return request.getRemoteAddr();
    }

    private boolean allowSuspended(String ip) {
        long now = System.currentTimeMillis();
        Deque<Long> hits = SUSPENDED_HITS.computeIfAbsent(ip, k -> new ArrayDeque<>());
        synchronized (hits) {
            while (!hits.isEmpty() && hits.peekFirst() < now - SUSPENDED_WINDOW_MS) hits.pollFirst();
            if (hits.size() >= SUSPENDED_MAX_REQ) return false;
            hits.addLast(now);
            return true;
        }
    }
}
