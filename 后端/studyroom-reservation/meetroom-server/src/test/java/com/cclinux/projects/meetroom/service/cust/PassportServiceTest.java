package com.cclinux.projects.meetroom.service.cust;

import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.JWTHelper;
import com.cclinux.projects.meetroom.comm.PwdHelper;
import com.cclinux.projects.meetroom.mapper.UserMapper;
import com.cclinux.projects.meetroom.model.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassportServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private PassportService passportService;

    @BeforeAll
    static void setUpStaticConfig() {
        AppConfig.JWT_SECERT = "unit-test-jwt-secret";
        AppConfig.JWT_CUST_EXPIRE = 604800L;
    }

    @Test
    void loginSuccessReturnsValidToken() {
        UserModel user = new UserModel();
        user.setUserId(1L);
        user.setUserAccount("student");
        user.setUserName("张三");
        user.setUserStatus(UserModel.STATUS.NORMAL);
        user.setUserPassword(PwdHelper.hash("123456"));
        user.setUserLoginTime(0);
        when(userMapper.getOne(any())).thenReturn(user);

        Map<String, Object> ret = passportService.login("student", "123456");

        assertNotNull(ret);
        assertEquals(1L, ret.get("id"));
        assertEquals("student", ret.get("account"));
        assertEquals("张三", ret.get("name"));
        assertEquals("尚未登录", ret.get("last"));
        assertEquals(604800L, ret.get("expire"));
        String token = (String) ret.get("token");
        assertNotNull(token);
        assertTrue(JWTHelper.check(token));
        assertEquals("cust", JWTHelper.getSub(token));
        verify(userMapper).update(any(), any());
    }

    @Test
    void loginWrongPasswordThrows() {
        when(userMapper.getOne(any())).thenReturn(null);

        AppException ex = assertThrows(AppException.class,
                () -> passportService.login("nobody", "wrong-pwd"));
        assertEquals("用户名或者密码错误", ex.getMessage());
    }

    @Test
    void loginLegacyMd5PasswordUpgradesToBcrypt() {
        UserModel user = new UserModel();
        user.setUserId(2L);
        user.setUserAccount("legacy");
        user.setUserName("老用户");
        user.setUserStatus(UserModel.STATUS.NORMAL);
        user.setUserPassword(cn.hutool.crypto.digest.DigestUtil.md5Hex("123456"));
        when(userMapper.getOne(any())).thenReturn(user);

        passportService.login("legacy", "123456");

        assertTrue(PwdHelper.verify("123456", user.getUserPassword()));
        assertTrue(user.getUserPassword().startsWith("$2"));
        verify(userMapper).update(any(), any());
    }

    @Test
    void loginForbiddenUserThrows() {
        UserModel user = new UserModel();
        user.setUserStatus(UserModel.STATUS.FORBID);
        user.setUserPassword(PwdHelper.hash("123456"));
        when(userMapper.getOne(any())).thenReturn(user);

        AppException ex = assertThrows(AppException.class,
                () -> passportService.login("forbid", "123456"));
        assertEquals("用户已禁用", ex.getMessage());
        verify(userMapper, never()).update(any(), any());
    }

    @Test
    void registerDuplicateAccountThrows() {
        when(userMapper.exists(any())).thenReturn(true);

        AppException ex = assertThrows(AppException.class,
                () -> passportService.register("dup", "昵称", "pwd123", "[]", "{}"));
        assertEquals("该用户名已经存在，请更换！", ex.getMessage());
    }

    @Test
    void registerSuccessStoresBcryptPassword() {
        when(userMapper.exists(any())).thenReturn(false);

        passportService.register("newbie", "新同学", "pwd123", "[]", "{}");

        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        verify(userMapper).add(captor.capture());
        UserModel saved = captor.getValue();
        assertEquals("newbie", saved.getUserAccount());
        assertEquals("新同学", saved.getUserName());
        assertTrue(saved.getUserPassword().startsWith("$2"));
        assertTrue(PwdHelper.verify("pwd123", saved.getUserPassword()));
        assertEquals(UserModel.STATUS.NORMAL, saved.getUserStatus());
    }

    @Test
    void editDuplicateAccountThrows() {
        when(userMapper.exists(any())).thenReturn(true);

        AppException ex = assertThrows(AppException.class,
                () -> passportService.edit(1L, "taken", "昵称", "[]", "{}"));
        assertEquals("该用户名已经存在，请更换！", ex.getMessage());
    }

    @Test
    void pwdWrongOldPasswordThrows() {
        when(userMapper.getOne(any())).thenReturn(null);

        AppException ex = assertThrows(AppException.class,
                () -> passportService.pwd(1L, "old-pwd", "new-pwd"));
        assertEquals("旧密码错误，请重新输入", ex.getMessage());
        verify(userMapper, never()).edit(any());
    }

    @Test
    void pwdSuccessUpdatesWithNewBcrypt() {
        UserModel user = new UserModel();
        user.setUserId(1L);
        user.setUserPassword(PwdHelper.hash("old-pwd"));
        when(userMapper.getOne(any())).thenReturn(user);

        passportService.pwd(1L, "old-pwd", "new-pwd");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<UpdateWhere<UserModel>> captor = ArgumentCaptor.forClass(UpdateWhere.class);
        verify(userMapper).edit(captor.capture());
        boolean matched = captor.getValue().getParamNameValuePairs().values().stream()
                .anyMatch(v -> v != null && PwdHelper.verify("new-pwd", v.toString()));
        assertTrue(matched);
    }
}
