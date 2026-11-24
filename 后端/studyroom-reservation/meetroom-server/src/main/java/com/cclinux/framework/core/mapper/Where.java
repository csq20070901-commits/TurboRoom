package com.cclinux.framework.core.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.regex.Pattern;


public class Where<T> extends QueryWrapper<T>  {

    // 排序列名白名单规则：仅允许字母开头、字母数字下划线组成，防止 ORDER BY SQL 注入
    private static final Pattern SAFE_COLUMN = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{0,63}$");

    /**
     * 前端直接传来排序字段
     */
    public QueryWrapper<T> fmtOrderBySort(String sortVal, String defaultSort) {
        if (StrUtil.contains(sortVal,"|")) {
            String[] arr = sortVal.split("\\|");

            if (arr.length >= 2 && SAFE_COLUMN.matcher(arr[0]).matches()) {
                if (arr[1].equals("desc"))
                    return this.orderByDesc(arr[0]);
                else
                    return this.orderByAsc(arr[0]);
            }
            // 非法列名直接忽略该排序请求，避免 SQL 注入

        }
        else if (StrUtil.isNotEmpty(defaultSort)){
            return this.orderByDesc(defaultSort);
        }

        return this.typedThis;
    }
}
