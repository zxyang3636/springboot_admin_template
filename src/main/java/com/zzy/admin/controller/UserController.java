package com.zzy.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.zzy.admin.common.Result;
import com.zzy.admin.domain.vo.UserVO;
import com.zzy.admin.exception.BusinessException;
import com.zzy.admin.exception.ParamException;
import org.springframework.web.bind.annotation.*;

/**
 * packageName com.zzy.admin.controller
 *
 * @author zzy
 * @className UserController
 * @date 2025/6/30
 * @description TODO
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @PostMapping("/login")
    public Result login(@RequestBody JSONObject jsonObject) {
        UserVO adminToken = UserVO.builder().token("Admin Token").build();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new ParamException();
        }
        if (username.equals("admin") && password.equals("111111")) {
            return Result.success(adminToken);
        }
        throw new BusinessException("账号或密码错误");
    }

    @GetMapping("/info")
    public Result info() {
        UserVO admin = UserVO.builder().avatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif")
                .username("admin").build();
        return Result.success(admin);
    }
}
