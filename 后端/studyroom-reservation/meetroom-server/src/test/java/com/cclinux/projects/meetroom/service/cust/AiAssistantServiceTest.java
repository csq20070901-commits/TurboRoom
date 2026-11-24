package com.cclinux.projects.meetroom.service.cust;

import com.cclinux.framework.exception.AppException;
import com.cclinux.projects.meetroom.mapper.MeetMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiAssistantServiceTest {

    @Mock
    private MeetService meetService;

    @Mock
    private MeetMapper meetMapper;

    @Mock
    private Environment env;

    @InjectMocks
    private AiAssistantService aiAssistantService;

    @Test
    void chatBlankQuestionRejected() {
        AppException ex = assertThrows(AppException.class,
                () -> aiAssistantService.chat("   ", null, null));
        assertEquals("请输入 1 至 2000 个字符的问题", ex.getMessage());
    }

    @Test
    void chatTooLongQuestionRejected() {
        String longQuestion = String.join("", Collections.nCopies(2001, "a"));
        AppException ex = assertThrows(AppException.class,
                () -> aiAssistantService.chat(longQuestion, null, null));
        assertEquals("请输入 1 至 2000 个字符的问题", ex.getMessage());
    }

    @Test
    void chatFailsGracefullyWhenAiNotConfigured() {
        when(env.getProperty(anyString())).thenReturn(null);

        AppException ex = assertThrows(AppException.class,
                () -> aiAssistantService.chat("你好", null, null));
        assertEquals("AI 助手尚未开通，请联系管理员启用后使用", ex.getMessage());
    }

    private Map<String, Object> candidate(boolean available, long remaining) {
        Map<String, Object> map = new HashMap<>();
        map.put("available", available);
        map.put("remaining", remaining);
        return map;
    }

    @Test
    void recommendSortsAvailableFirstThenByRemainingAndTruncates() {
        List<Map<String, Object>> candidates = new ArrayList<>();
        candidates.add(candidate(true, 5));
        candidates.add(candidate(false, 2));
        candidates.add(candidate(true, 1));
        candidates.add(candidate(true, 3));
        when(meetService.getAvailability(eq("2026-11-12"), eq("09:00-10:00"), eq(4), eq("全部")))
                .thenReturn(candidates);

        List<Map<String, Object>> result =
                aiAssistantService.recommend("2026-11-12", "09:00-10:00", 4, "全部", "需要靠窗");

        assertEquals(3, result.size());
        assertEquals(1L, ((Number) result.get(0).get("remaining")).longValue());
        assertEquals(3L, ((Number) result.get(1).get("remaining")).longValue());
        assertEquals(5L, ((Number) result.get(2).get("remaining")).longValue());
        assertTrue(Boolean.TRUE.equals(result.get(0).get("available")));
        for (Map<String, Object> item : result) {
            assertNotNull(item.get("reason"));
        }
        assertTrue(((String) result.get(0).get("reason")).contains("剩余 1 个名额"));
        assertTrue(((String) result.get(0).get("reason")).contains("需要靠窗"));
    }

    @Test
    void recommendKeepsUnavailableAsFallback() {
        List<Map<String, Object>> candidates = new ArrayList<>();
        candidates.add(candidate(false, 1));
        candidates.add(candidate(false, 3));
        when(meetService.getAvailability(anyString(), anyString(), anyInt(), any())).thenReturn(candidates);

        List<Map<String, Object>> result =
                aiAssistantService.recommend("2026-11-12", "09:00-10:00", 5, "", "");

        assertEquals(2, result.size());
        for (Map<String, Object> item : result) {
            assertTrue(((String) item.get("reason")).contains("候补参考"));
        }
    }

    @Test
    void recommendRejectsMissingParams() {
        AppException ex = assertThrows(AppException.class,
                () -> aiAssistantService.recommend("", "09:00-10:00", 2, "", ""));
        assertEquals("请填写预约日期、时段和人数后再获取实时推荐", ex.getMessage());
        verifyNoInteractions(meetService);
    }
}
