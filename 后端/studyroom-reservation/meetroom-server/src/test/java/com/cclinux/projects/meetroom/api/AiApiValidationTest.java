package com.cclinux.projects.meetroom.api;

import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.helper.JWTHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AiApiValidationTest extends BaseApiTest {

    private String custToken() {
        // 在测试方法内设置，避免被 Spring 上下文首次创建时的 yml 注入覆盖
        AppConfig.JWT_SECERT = "ai-api-validation-test-secret";
        return JWTHelper.genToken(1L, "student", "cust", 604800L);
    }

    @Test
    void aiChatWithoutTokenRejected() throws Exception {
        mockMvc.perform(post("/meetroom/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"你好\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2401));
    }

    @Test
    void aiRecommendWithoutTokenRejected() throws Exception {
        mockMvc.perform(post("/meetroom/ai/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2401));
    }

    @Test
    void aiChatBlankQuestionRejectedWithToken() throws Exception {
        mockMvc.perform(post("/meetroom/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", custToken())
                        .header("sub", "cust")
                        .content("{\"question\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1600))
                .andExpect(jsonPath("$.msg").value("请输入 1 至 2000 个字符的问题"));
    }

    @Test
    void aiChatTooLongQuestionRejectedWithToken() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2001; i++) sb.append('a');
        mockMvc.perform(post("/meetroom/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", custToken())
                        .header("sub", "cust")
                        .content("{\"question\":\"" + sb + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1600))
                .andExpect(jsonPath("$.msg").value("请输入 1 至 2000 个字符的问题"));
    }

    @Test
    void aiRecommendMissingParamsRejectedWithToken() throws Exception {
        mockMvc.perform(post("/meetroom/ai/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", custToken())
                        .header("sub", "cust")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1600))
                .andExpect(jsonPath("$.msg").value("请填写预约日期、时段和人数后再获取实时推荐"));
    }
}
