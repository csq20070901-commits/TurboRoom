package com.cclinux.projects.meetroom.api;

import com.cclinux.SmartWorkApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = SmartWorkApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public abstract class BaseApiTest {

    @Autowired
    protected MockMvc mockMvc;
}
