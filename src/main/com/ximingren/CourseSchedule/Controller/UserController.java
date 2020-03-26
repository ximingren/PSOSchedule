package com.ximingren.CourseSchedule.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ximingren.CourseSchedule.Bean.po.Permission;
import com.ximingren.CourseSchedule.Bean.po.User;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description 暂时不提供修改密码等个人功能
 * @Author ximingren
 * @Date 2020/3/10 14:55
 */
@RestController()
public class UserController {
    @Autowired
    IUserService userService;

    private Map<String, User> userMap = new HashMap<>();
    private final BASE64Encoder encoder = new BASE64Encoder();

    @RequestMapping(value = "/user/login")
    public ResultVO UserLogin(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        String password = requestUser.getPassword();
        String loginType = requestUser.getLoginType();
        User user = null;
        if (username.equals("admin")) {
            user = userService.login(username, password);
        } else {
            if (loginType.equals("1")) {
                //学生
                user = userService.loginStudent(username, password);

            } else if (loginType.equals("2")) {
                //教师
                user = userService.loginTeacher(username, password);
            }
        }

        if (user != null) {
            long time = System.currentTimeMillis();
            String timestamp = String.valueOf(time / 1000);
            String token = encoder.encode((username + timestamp).getBytes());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            userMap.put(token, user);
            user.setRoleName(user.getPermissions().get(0).getRolename());
            return ResultVO.ok("登录成功! 欢迎你，" + user.getUsername(), jsonObject);
        } else {
            return ResultVO.faile("登录失败");
        }
    }

    @RequestMapping(value = "/user/info")
    public ResultVO UserInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        User user = userMap.get(token);
        if (user != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("roleName", user.getRoleName());
            jsonObject.put("userName", user.getUsername());
            jsonObject.put("name", user.getCondition());
            JSONArray jsonArray = new JSONArray();
            for (Permission permission : user.getPermissions()) {
                jsonArray.add(permission.getPermission());
            }
            jsonObject.put("data", jsonArray);
            return ResultVO.ok("获取权限成功，角色为" + user.getRoleName(), jsonObject);
        } else {
            return ResultVO.faile("获取权限失败");
        }
    }
}
