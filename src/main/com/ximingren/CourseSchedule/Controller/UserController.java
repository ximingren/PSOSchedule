package com.ximingren.CourseSchedule.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 14:55
 */
@RestController()
public class UserController {
    @RequestMapping(value = "/user/login")
    public ResultVO UserLogin() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", "123");
        return ResultVO.ok("获取token成功", jsonObject);
    }

    @RequestMapping(value = "/user/info")
    public ResultVO UserInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roles", "admin");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("course-manage");
        jsonArray.add("class-task");
        jsonArray.add("course-individual");
        jsonArray.add("education-manage");
        jsonArray.add("college-manage");
        jsonArray.add("teacher-manage");
        jsonArray.add("teachBuild-manage");
        jsonArray.add("students-manage");
        jsonArray.add("review-manage");
        jsonArray.add("return-goods");
        jsonArray.add("goods");
        jsonArray.add("goods-list");
        jsonArray.add("goods-classify");
        jsonArray.add("permission");
        jsonArray.add("user-manage");
        jsonArray.add("role-manage");
        jsonArray.add("menu-manage");
        jsonObject.put("data", jsonArray);
        return ResultVO.ok("获取token成功", jsonObject);
    }
}
