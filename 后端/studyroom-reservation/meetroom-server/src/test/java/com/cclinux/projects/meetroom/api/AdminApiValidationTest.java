package com.cclinux.projects.meetroom.api;

import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.helper.JWTHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 管理端接口仅允许管理员 token 访问，学生 token 或缺少 token 一律 2401。 */
class AdminApiValidationTest extends BaseApiTest {

    private String custToken() {
        AppConfig.JWT_SECERT = "admin-api-validation-test-secret";
        return JWTHelper.genToken(1L, "student", "cust", 604800L);
    }

    @Test
    void adminEndpointWithoutTokenRejected() throws Exception {
        mockMvc.perform(post("/meetroom/admin/mgr/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2401));
    }

    @Test
    void adminEndpointWithStudentTokenRejected() throws Exception {
        mockMvc.perform(post("/meetroom/admin/mgr/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", custToken())
                        .header("sub", "cust")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2401));
    }

    @Test
    void adminEndpointWithAdminTokenSucceeds() throws Exception {
        AppConfig.JWT_SECERT = "admin-api-validation-test-secret";
        String adminToken = JWTHelper.genToken(1L, "admin", "admin", 86400L);
        mockMvc.perform(post("/meetroom/admin/mgr/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("token", adminToken)
                        .header("sub", "admin")
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
