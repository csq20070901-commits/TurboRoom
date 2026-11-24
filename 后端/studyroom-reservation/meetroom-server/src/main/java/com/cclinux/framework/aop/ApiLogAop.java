package com.cclinux.framework.aop;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.exception.AppExceptionHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Aspect
@Component
public class ApiLogAop {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 连接点
     */
    @Pointcut("execution(public * com.cclinux.projects.*.controller..*.*(..))")
    public void aop() {
    }


    /**
     * 在切点之前织入
     */
    @Before("aop()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();


        String url = request.getRequestURL().toString();

        // 打印请求相关参数（token 与密码类字段脱敏，禁止明文入日志）
        logger.info("========================================== Start ==========================================");
        logger.info("URL            : {}", url);
        logger.info("Header(token)  : {}", maskToken(request.getHeader("token")));
        logger.info("HTTP Method    : {}", request.getMethod());
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("IP             : {}", request.getRemoteAddr());
        try {
            logger.info("Request Args   : \n{}", maskSensitive(joinPoint.getArgs()));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }


    /**
     * 环绕，若抛出异常则不走完around，要额外处理
     */
    @Around("aop()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = pjp.proceed();

            // 打印出参
            logger.info("Response Args  : \n{}", maskSensitive(result));

        } catch (AppException ex) {
            AppExceptionHandler handler = new AppExceptionHandler();
            logger.info("Response Args  : \n{}", maskSensitive(handler.handleAppException(ex)));

            throw ex;
        } catch (RuntimeException ex) {
            AppExceptionHandler handler = new AppExceptionHandler();
            logger.info("Response Args  : \n{}", maskSensitive(handler.handleRuntimeException(ex)));

            throw ex;
        } catch (Exception ex) {
            AppExceptionHandler handler = new AppExceptionHandler();
            logger.info("Response Args  : \n{}", maskSensitive(handler.handleOtherException(ex)));

            throw ex;
        } finally {
            logger.info("Duration       : {} ms", System.currentTimeMillis() - startTime);
            logger.info("=========================================== End ===========================================" + LINE_SEPARATOR);
        }

        return result;
    }

    private String maskToken(String token) {
        if (token == null || token.length() <= 10) return "******";
        return token.substring(0, 6) + "******";
    }

    private String maskSensitive(Object obj) {
        try {
            if (obj instanceof Object[] || obj instanceof java.util.Collection) {
                JSONArray arr = JSONUtil.parseArray(JSONUtil.toJsonStr(obj));
                maskArray(arr);
                return JSONUtil.toJsonPrettyStr(arr);
            }
            JSONObject parsed = JSONUtil.parseObj(obj, false);
            maskNode(parsed);
            return JSONUtil.toJsonPrettyStr(parsed);
        } catch (Exception ex) {
            return String.valueOf(obj);
        }
    }

    private void maskArray(JSONArray arr) {
        for (Object item : arr) {
            if (item instanceof JSONObject) maskNode((JSONObject) item);
            else if (item instanceof JSONArray) maskArray((JSONArray) item);
        }
    }

    private void maskNode(JSONObject node) {
        for (String key : new ArrayList<>(node.keySet())) {
            Object val = node.get(key);
            String lower = key.toLowerCase();
            if (lower.contains("password") || lower.contains("pwd") || lower.contains("token")
                    || lower.contains("secret") || lower.contains("apikey") || lower.contains("api-key")
                    || lower.contains("api_key")) {
                node.set(key, "******");
            } else if (val instanceof JSONObject) {
                maskNode((JSONObject) val);
            } else if (val instanceof JSONArray) {
                maskArray((JSONArray) val);
            } else if (val instanceof Map) {
                maskNode(new JSONObject((Map<?, ?>) val));
            }
        }
    }

}
