package com.cclinux.projects.meetroom.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeApiTest extends BaseApiTest {

    @Test
    void homeListReturnsData() throws Exception {
        mockMvc.perform(post("/meetroom/home/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void setupGetReturnsSuccess() throws Exception {
        mockMvc.perform(post("/meetroom/setup/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"TEST_KEY\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
