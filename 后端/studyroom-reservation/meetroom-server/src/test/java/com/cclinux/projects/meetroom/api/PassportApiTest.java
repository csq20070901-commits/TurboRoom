package com.cclinux.projects.meetroom.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class PassportApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> registerBody(String account) {
        Map<String, Object> body = new HashMap<>();
        body.put("account", account);
        body.put("name", "测试同学");
        body.put("password", "abc123456");
        body.put("forms", "[]");
        body.put("obj", "{}");
        return body;
    }

    @Test
    void registerThenLoginThenMyDetailSucceeds() throws Exception {
        String account = "test" + System.currentTimeMillis();

        String regResp = mockMvc.perform(post("/meetroom/passport/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("account", account);
        loginBody.put("password", "abc123456");

        String loginResp = mockMvc.perform(post("/meetroom/passport/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token.account").value(account))
                .andExpect(jsonPath("$.data.token.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(loginResp);
        String token = root.path("data").path("token").path("token").asText();

        mockMvc.perform(post("/meetroom/passport/my/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .header("token", token)
                        .header("sub", "cust"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void loginWrongPasswordFails() throws Exception {
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("account", "student");
        loginBody.put("password", "wrong-password-xyz");

        mockMvc.perform(post("/meetroom/passport/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1600))
                .andExpect(jsonPath("$.msg").value("用户名或者密码错误"));
    }

    @Test
    void duplicateRegisterRejected() throws Exception {
        String account = "dup" + System.currentTimeMillis();

        mockMvc.perform(post("/meetroom/passport/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody(account))))
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(post("/meetroom/passport/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1600))
                .andExpect(jsonPath("$.msg").value("该用户名已经存在，请更换！"));
    }

    @Test
    void protectedEndpointRequiresToken() throws Exception {
        mockMvc.perform(post("/meetroom/passport/my/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2401));
    }
}
