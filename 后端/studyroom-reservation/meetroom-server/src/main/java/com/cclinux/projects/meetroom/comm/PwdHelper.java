package com.cclinux.projects.meetroom.comm;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.DigestUtil;

/**
 * 密码工具：新密码统一使用 BCrypt 存储；
 * 对历史无盐 MD5 密码保持登录兼容，登录成功后由业务方调用 hash() 升级为 BCrypt。
 */
public final class PwdHelper {

    private PwdHelper() {
    }

    public static String hash(String plain) {
        return BCrypt.hashpw(plain);
    }

    public static boolean verify(String plain, String stored) {
        if (StrUtil.isBlank(plain) || StrUtil.isBlank(stored)) return false;
        if (isLegacy(stored)) return DigestUtil.md5Hex(plain).equalsIgnoreCase(stored);
        return BCrypt.checkpw(plain, stored);
    }

    /** 历史 MD5 密码（非 BCrypt 格式）返回 true，登录成功后应升级 */
    public static boolean isLegacy(String stored) {
        return StrUtil.isNotBlank(stored) && !stored.startsWith("$2");
    }
}
