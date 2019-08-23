package com.zcs.yunjia.controller;

import com.zcs.yunjia.pojo.LoginResult;
import com.zcs.yunjia.pojo.RequestResult;
import com.zcs.yunjia.utils.CookieUtils;
import com.zcs.yunjia.utils.JsonUtils;
import com.zcs.yunjia.pojo.TbUser;
import com.zcs.yunjia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 跳转页面
     * @param page
     */
    @RequestMapping(value = "/yunjia/{page}",method = RequestMethod.GET)
    public String showPage(@PathVariable("page") String page, String redirect, Model model){
        model.addAttribute("redirect",redirect);
        return page;
    }

    /**
     * 用户登录校验
     * @param username 用户名
     * @param password 密码
     * @return LoginResult
     */
    @RequestMapping(value="/user/login",method = RequestMethod.POST)
    @ResponseBody
    public LoginResult validateUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password){
        LoginResult result = userService.login(username,password);
        if(result.getMsg() != "") {
            CookieUtils.setCookie(request, response, "userToken", result.getMsg());
        }
        return result;
    }

    @RequestMapping(value="/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public boolean checkData(@PathVariable("param") String param,@PathVariable("type") String type ){
        return userService.checkData(param,type);
    }

    @RequestMapping("/check/{token}")
    @ResponseBody
    public String checkToken(@PathVariable String token){
        String json = JsonUtils.objectToJson(userService.checkToken(token));
        //String jsonp = callback+"("+json+")";
        System.out.println("controller:checktoken"+token);
        return json;
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public RequestResult register(String username,String password,String phone){
        TbUser user = new TbUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        return userService.register(user);
    }
    @RequestMapping("/user/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request,"userToken");
        RequestResult result = userService.logout(token);
        if(result.getStatus() == 200){
            //清空cookie
            CookieUtils.deleteCookie(request,response,"userToken");
        }
        return "index";
    }
}
