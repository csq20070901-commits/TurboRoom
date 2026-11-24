package com.cclinux.projects.meetroom.comm;

import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.exception.AppException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Locale;

/** OpenAI 兼容协议的大模型客户端：AI 助手与 AI 测试中心共用。密钥读取优先级：JVM -D 参数 > 环境变量 > application.yml。 */
@Component("MeetRoomAiClient")
public class AiClient {
    private static final Logger LOG = LoggerFactory.getLogger(AiClient.class);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8)).build();

    private final Environment env;

    public AiClient(Environment env) {
        this.env = env;
    }

    public String chat(String systemPrompt, String userPrompt, int maxTokens) {
        String enabled = setting("AI_ENABLED");
        String apiKey = setting("AI_API_KEY");
        if (!"true".equalsIgnoreCase(enabled) || StrUtil.isBlank(apiKey)) {
            LOG.warn("AI not configured: AI_ENABLED={}, AI_API_KEY blank={}", enabled, StrUtil.isBlank(apiKey));
            throw new AppException("AI 功能尚未开通，请检查 AI 配置后重启服务");
        }
        String baseUrl = setting("AI_BASE_URL");
        if (StrUtil.isBlank(baseUrl)) baseUrl = "https://api.openai.com/v1";
        String model = setting("AI_MODEL");
        if (StrUtil.isBlank(model)) model = "gpt-4o-mini";

        JsonArray messages = new JsonArray();
        messages.add(message("system", systemPrompt));
        messages.add(message("user", userPrompt));
        JsonObject payload = new JsonObject();
        payload.addProperty("model", model);
        payload.add("messages", messages);
        payload.addProperty("temperature", 0.2);
        payload.addProperty("max_tokens", maxTokens);

        try {
            String endpoint = baseUrl.replaceAll("/+$", "") + "/chat/completions";
            HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint)).timeout(Duration.ofSeconds(90))
                    .header("Content-Type", "application/json").header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString())).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String detail = response.body();
                if (detail != null && detail.length() > 500) detail = detail.substring(0, 500);
                LOG.warn("AI provider error, HTTP {}: {}", response.statusCode(), StrUtil.blankToDefault(detail, "(no body)"));
                throw new AppException("AI 服务暂时不可用，请稍后再试");
            }
            String answer = JsonParser.parseString(response.body()).getAsJsonObject().getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message").get("content").getAsString();
            if (StrUtil.isBlank(answer)) throw new AppException("AI 未返回有效内容，请稍后重试。");
            return answer.trim();
        } catch (AppException e) {
            throw e;
        } catch (HttpTimeoutException e) {
            LOG.warn("AI model request timed out; model={}, timeoutSeconds=90, exception={}", model, e.getClass().getSimpleName());
            throw new AppException("AI 响应超时，请稍后再试");
        } catch (Exception e) {
            LOG.warn("AI request failed: {}", e.toString());
            throw new AppException("AI 请求失败，请稍后再试");
        }
    }

    /** JVM -D 参数优先，其次读取操作系统环境变量，最后读取 Spring application.yml，避免将密钥提交进项目文件。 */
    public String setting(String key) {
        String value = System.getProperty(key);
        if (StrUtil.isNotBlank(value)) return value;
        value = System.getenv(key);
        if (StrUtil.isNotBlank(value)) return value;
        String yamlKey = "ai." + key.substring(3).toLowerCase(Locale.ROOT).replace('_', '-');
        return env.getProperty(yamlKey);
    }

    private JsonObject message(String role, String content) {
        JsonObject m = new JsonObject();
        m.addProperty("role", role);
        m.addProperty("content", content);
        return m;
    }
}
