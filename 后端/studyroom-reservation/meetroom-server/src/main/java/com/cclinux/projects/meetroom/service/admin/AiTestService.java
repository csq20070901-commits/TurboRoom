package com.cclinux.projects.meetroom.service.admin;

import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.exception.AppException;
import com.cclinux.projects.meetroom.comm.AiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 测试中心：把需求描述交给大模型生成接口测试用例，再真实调用接口执行并给出通过/失败结论。
 * 生成的用例只做只读校验（登录、参数校验、鉴权等），不会触发任何写入类管理操作。
 */
@Service("MeetRoomAiTestService")
public class AiTestService extends BaseMyAdminService {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5)).build();

    private static final String GENERATE_PROMPT =
            "你是 TurboRoom 校园自习预约系统的接口测试专家。请根据用户的需求描述生成接口测试用例。\n" +
            "要求：\n" +
            "1. 只输出一个 JSON 数组，不要输出任何其他文字或 Markdown 代码块标记。\n" +
            "2. 每个元素是一个对象，字段：name（用例名）、method（GET 或 POST）、path（接口路径，以 / 开头，不要带 /meetroom 前缀）、body（请求体 JSON 对象，GET 可为空对象）、expect（预期结果的中文描述）、expectCode（预期返回的业务 code 数字，如 200、1600、2401）。\n" +
            "3. 常见接口：POST /home/list（首页数据，expectCode 200）、POST /setup/get（参数 key，expectCode 200）、POST /passport/login（参数 account/password，成功 200、密码错误 1600）、POST /passport/register（参数 account/name/password/forms/obj，重复账号 1600）、POST /meet/list（分页参数，expectCode 200）、POST /news/list（分页参数，expectCode 200）、POST /ai/chat（需登录，未登录 2401）、POST /ai/recommend（需登录，未登录 2401）、POST /passport/my/detail（不带 token 访问 2401）、POST /admin/login（参数 name/password，成功 200）。\n" +
            "4. 用例最多 20 条，尽量覆盖成功路径和异常路径。";

    @Resource(name = "MeetRoomAiClient")
    private AiClient aiClient;

    @Autowired
    private Environment env;

    public List<Map<String, Object>> generate(String requirement) {
        if (StrUtil.isBlank(requirement) || requirement.trim().length() > 2000)
            throw new AppException("请输入 1 至 2000 个字符的需求描述");

        String answer = aiClient.chat(GENERATE_PROMPT, requirement.trim(), 3000);
        JsonArray array = parseCases(answer);
        if (array == null) {
            logger.warn("AI 测试用例首次解析失败，原始返回：{}", answer);
            String retryAnswer = aiClient.chat(
                    GENERATE_PROMPT + "上一次的回答格式不正确，无法解析。请再次强调：只输出一个 JSON 数组，"
                            + "数组元素为包含 name/method/path/body/expect/expectCode 字段的对象，不要输出任何解释文字或代码块标记。",
                    requirement.trim(), 3000);
            array = parseCases(retryAnswer);
            if (array == null) {
                logger.warn("AI 测试用例重试解析失败，原始返回：{}", retryAnswer);
                throw new AppException("AI 返回内容无法解析为测试用例，请调整需求描述后重试。");
            }
        }

        List<Map<String, Object>> cases = new ArrayList<>();
        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;
            JsonObject obj = element.getAsJsonObject();
            String path = obj.has("path") ? obj.get("path").getAsString() : "";
            path = normalizePath(path);
            if (path.isEmpty()) continue;
            Map<String, Object> item = new HashMap<>();
            item.put("name", obj.has("name") && StrUtil.isNotBlank(obj.get("name").getAsString()) ? obj.get("name").getAsString() : path);
            String method = obj.has("method") ? obj.get("method").getAsString().toUpperCase() : "POST";
            if (!"GET".equals(method) && !"POST".equals(method)) method = "POST";
            item.put("method", method);
            item.put("path", path);
            JsonObject body = obj.has("body") && obj.get("body").isJsonObject() ? obj.get("body").getAsJsonObject() : new JsonObject();
            item.put("body", new com.google.gson.Gson().fromJson(body.toString(), Map.class));
            item.put("expect", obj.has("expect") ? obj.get("expect").getAsString() : "");
            if (obj.has("expectCode") && obj.get("expectCode").isJsonPrimitive()) {
                try {
                    item.put("expectCode", obj.get("expectCode").getAsInt());
                } catch (Exception ignored) {
                }
            }
            cases.add(item);
            if (cases.size() >= 20) break;
        }
        if (cases.isEmpty()) throw new AppException("AI 未生成有效用例，请调整需求描述后重试。");
        return cases;
    }

    private JsonArray parseCases(String raw) {
        String json = raw == null ? "" : raw.trim();
        String cleaned = json.replaceAll(",\\s*([}\\]])", "$1");
        JsonArray array = tryParseArray(cleaned);
        if (array != null) return array;
        try {
            JsonElement root = JsonParser.parseString(cleaned);
            if (root.isJsonObject() && root.getAsJsonObject().has("cases")) {
                return root.getAsJsonObject().get("cases").getAsJsonArray();
            }
        } catch (Exception ignored) {
        }
        int fenceStart = cleaned.indexOf("[");
        int fenceEnd = cleaned.lastIndexOf("]");
        if (fenceStart >= 0 && fenceEnd > fenceStart) return tryParseArray(cleaned.substring(fenceStart, fenceEnd + 1));
        return null;
    }

    private JsonArray tryParseArray(String json) {
        try {
            return JsonParser.parseString(json).getAsJsonArray();
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> run(List<Map<String, Object>> cases) {
        if (cases == null || cases.isEmpty()) cases = regression();
        if (cases.size() > 50) cases = cases.subList(0, 50);

        String baseUrl = env.getProperty("serverUrl");
        if (StrUtil.isBlank(baseUrl)) baseUrl = "http://localhost:8888";
        baseUrl = baseUrl.replaceAll("/+$", "") + "/meetroom";

        List<Map<String, Object>> results = new ArrayList<>();
        int pass = 0;
        for (Map<String, Object> item : cases) {
            Map<String, Object> row = executeOne(baseUrl, item);
            results.add(row);
            if (Boolean.TRUE.equals(row.get("pass"))) pass++;
        }
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", results.size());
        summary.put("pass", pass);
        summary.put("fail", results.size() - pass);

        Map<String, Object> ret = new HashMap<>();
        ret.put("summary", summary);
        ret.put("results", results);
        return ret;
    }

    public List<Map<String, Object>> regression() {
        List<Map<String, Object>> cases = new ArrayList<>();
        cases.add(builtin("首页数据接口", "POST", "/home/list", "{}", 200, "返回首页场地与公告数据"));
        cases.add(builtin("学生登录成功", "POST", "/passport/login", "{\"account\":\"student\",\"password\":\"123456\"}", 200, "返回 token 与用户信息"));
        cases.add(builtin("学生登录密码错误", "POST", "/passport/login", "{\"account\":\"student\",\"password\":\"wrong-pwd\"}", 1600, "提示用户名或者密码错误"));
        cases.add(builtin("未登录访问个人信息", "POST", "/passport/my/detail", "{}", 2401, "缺少 token 被拦截"));
        cases.add(builtin("未登录访问 AI 问答被拦截", "POST", "/ai/chat", "{\"question\":\"你好\"}", 2401, "缺少 token 被拦截"));
        cases.add(builtin("未登录访问 AI 推荐被拦截", "POST", "/ai/recommend", "{}", 2401, "缺少 token 被拦截"));
        cases.add(builtin("未登录访问管理端被拦截", "POST", "/admin/mgr/list", "{}", 2401, "学生 token 或缺少 token 均不可访问管理接口"));
        cases.add(builtin("管理员登录成功", "POST", "/admin/login", "{\"name\":\"admin\",\"password\":\"123456\"}", 200, "返回管理员 token"));
        cases.add(builtin("系统设置读取", "POST", "/setup/get", "{\"key\":\"TEST_KEY\"}", 200, "返回设置内容或空"));
        return cases;
    }

    private Map<String, Object> builtin(String name, String method, String path, String body, int expectCode, String expect) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("method", method);
        item.put("path", path);
        item.put("body", new com.google.gson.Gson().fromJson(body, Map.class));
        item.put("expect", expect);
        item.put("expectCode", expectCode);
        return item;
    }

    private Map<String, Object> executeOne(String baseUrl, Map<String, Object> item) {
        Map<String, Object> row = new HashMap<>();
        String name = String.valueOf(item.getOrDefault("name", "未命名用例"));
        String method = String.valueOf(item.getOrDefault("method", "POST")).toUpperCase();
        String path = normalizePath(String.valueOf(item.getOrDefault("path", "")));
        row.put("name", name);
        row.put("method", method);
        row.put("path", path);
        row.put("expect", String.valueOf(item.getOrDefault("expect", "")));
        if (path.isEmpty()) {
            row.put("status", 0);
            row.put("code", 0);
            row.put("duration", 0);
            row.put("pass", false);
            row.put("msg", "用例缺少有效的接口路径");
            return row;
        }
        Object bodyObj = item.getOrDefault("body", new JsonObject());
        String body;
        if (bodyObj instanceof JsonObject) body = bodyObj.toString();
        else if (bodyObj instanceof Map) body = new com.google.gson.Gson().toJson(bodyObj);
        else body = String.valueOf(bodyObj);

        long start = System.currentTimeMillis();
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json");
            if ("GET".equals(method)) {
                builder.GET();
            } else {
                builder.POST(HttpRequest.BodyPublishers.ofString(body));
            }
            HttpResponse<String> response = HTTP_CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            long duration = System.currentTimeMillis() - start;
            row.put("status", response.statusCode());
            row.put("duration", duration);
            Integer code = parseCode(response.body());
            row.put("code", code == null ? -1 : code);
            boolean pass = response.statusCode() == 200 && code != null
                    && (!item.containsKey("expectCode") || code.equals(((Number) item.get("expectCode")).intValue()));
            row.put("pass", pass);
            row.put("msg", pass ? "符合预期" : "HTTP " + response.statusCode() + "，业务 code " + (code == null ? "无法解析" : code));
        } catch (Exception e) {
            row.put("status", 0);
            row.put("code", -1);
            row.put("duration", System.currentTimeMillis() - start);
            row.put("pass", false);
            row.put("msg", "请求失败：" + e.getClass().getSimpleName() + "（请确认后端服务已启动）");
        }
        return row;
    }

    private Integer parseCode(String body) {
        if (StrUtil.isBlank(body)) return null;
        try {
            JsonElement element = JsonParser.parseString(body);
            if (element.isJsonObject() && element.getAsJsonObject().has("code")) {
                return element.getAsJsonObject().get("code").getAsInt();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String normalizePath(String path) {
        if (path == null) return "";
        path = path.trim();
        if (path.startsWith("/meetroom")) path = path.substring("/meetroom".length());
        return path.startsWith("/") ? path : "";
    }
}
