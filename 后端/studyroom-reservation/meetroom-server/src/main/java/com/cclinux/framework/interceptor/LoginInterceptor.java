package com.cclinux.framework.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.constants.AppCode;
import com.cclinux.framework.core.domain.LoginDTO;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.JWTHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 路由不存在
        String uri = request.getRequestURI();
        if (StrUtil.equals(uri, "/error")) throw new AppException("页面不存在");

        LoginIgnore loginAnnotation;

        if (handler instanceof HandlerMethod) {
            loginAnnotation = ((HandlerMethod) handler).getMethodAnnotation(LoginIgnore.class);

            DemoShow demoAnnotation;
            demoAnnotation = ((HandlerMethod) handler).getMethodAnnotation(DemoShow.class);
            if (ObjectUtil.isNotEmpty(demoAnnotation) && AppConfig.IS_DEMO) {
                throw new AppException("当前为演示模式，管理端操作仅作展示，不会实际生效");
            }

        } else {
            return true;
        }

        // 登录认证
        String checkMsg = this.checkToken(request);

        if (StrUtil.isNotEmpty(checkMsg)) {
            if (ObjectUtil.isNull(loginAnnotation)) {
                // 需要登录但校验不通过
                throw new AppException(checkMsg, AppCode.ERROR_TOKEN);
            } else {

                //  未登录状态
                request.setAttribute("userId", 0);

            }
        } else {
            //登录校验已经通过，直接设置登录信息到request里
            String token = request.getHeader("token");
            String sub = request.getHeader("sub");
            if (StrUtil.isNotEmpty(token) && StrUtil.isNotEmpty(sub)) {
                LoginDTO login = JWTHelper.getToken(token);
                long id = login.getId();

                if (sub.equals("cust")) {
                    request.setAttribute("userId", id);
                } else if (sub.equals("admin")) {
                    request.setAttribute("adminId", id);
                } else if (sub.equals("work")) {
                    request.setAttribute("workId", id);
                }
            }

            // 管理端接口仅允许管理员身份访问，防止学生 token 越权调用 /admin/**
            if (ObjectUtil.isNull(loginAnnotation) && StrUtil.contains(uri, "/admin/")
                    && request.getAttribute("adminId") == null) {
                throw new AppException("登录已失效，请重新登录", AppCode.ERROR_TOKEN);
            }
        }

        return true;
    }

    // 业务处理
    private String checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        String sub = request.getHeader("sub");

        // 技术原因只写日志，返回给用户的是友好提示
        String techMsg = "";

        if (StrUtil.isEmpty(token))
            techMsg = "token is null";
        else if (StrUtil.isEmpty(sub))
            techMsg = "header's sub is empty";
        else if (!JWTHelper.check(token))
            techMsg = "token check error";
        else {
            String tokenSub = JWTHelper.getSub(token);

            if (StrUtil.isEmpty(tokenSub))
                techMsg = "token's sub is empty";
            else if (!tokenSub.equals(sub))
                techMsg = "sub verify error";
        }

        // AOP无法在拦截器前输出日志（不打印 token 等敏感信息）
        logger.info("========================================== Start ==========================================");
        logger.info("URL            : {}", request.getRequestURL().toString());
        logger.info("Header(sub)    : {}", sub);
        logger.info("HTTP Method    : {}", request.getMethod());
        logger.info("IP             : {}", request.getRemoteAddr());
        if (StrUtil.isNotEmpty(techMsg)) {
            logger.info("Response Args  : {}", "{\"msg\":" + techMsg + ",\"code\":" + AppCode.ERROR_TOKEN + "}");
        }

        logger.info("=========================================== End ===========================================");

        return StrUtil.isNotEmpty(techMsg) ? "登录已失效，请重新登录" : "";

    }

}
