package com.example.controller;

import com.example.pojo.User;
import com.example.service.UserService;
import com.example.util.CheckCodeUtil;
import com.example.util.WebSocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService service;

    @PostMapping
    public Result register(@RequestBody User user, HttpSession session) {

        String checkCode = (String) session.getAttribute("checkCodeStr");
        if(!user.getCode().equalsIgnoreCase(checkCode)) {
            return new Result(false, Code.REGISTER_ERR, "验证码错误");
        }

        boolean b = service.register(user);
        return new Result(b, (b?Code.REGISTER_OK:Code.REGISTER_ERR), b?"":"用户名已存在");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpSession session) {
        boolean b = false;
        User user1 = service.select(user.getUsername());
        if(user1 != null) {
            if(user1.getPassword().equals(user.getPassword())) {
                b = true;
            }
        }
        if(b) {
            session.setAttribute("username", user.getUsername());
        }
        return new Result(b, (b?Code.LOGIN_OK:Code.LOGIN_ERR), "用户名或密码错误");
    }

    @GetMapping(value = "/checkcode", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImg(HttpSession session) throws IOException {
        File file = new File("E:\\javaProjects\\newArea\\simchat\\src\\main\\resources\\static\\img\\checkcode.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        FileOutputStream outputStream = new FileOutputStream(file);
        String checkCode = CheckCodeUtil.outputVerifyImage(85, 45, outputStream,4);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());

        session.setAttribute("checkCodeStr", checkCode);

        return bytes;
    }

    @GetMapping("/name")
    public String getName(HttpSession session) {
        return (String) session.getAttribute("username");
    }

    @GetMapping("/{username}")
    public Integer exists(@PathVariable String username) {
        if(WebSocketUtils.ONLINE_USER_SESSIONS.containsKey(username)) {
            return Code.EXIST;
        }
        return Code.NOT_EXIST;
    }
}
