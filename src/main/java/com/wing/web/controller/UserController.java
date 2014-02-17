package com.wing.web.controller;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * user controller
 *
 * Created by wingzhao on 14-2-16.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * 获得用户信息
     *
     * @return
     */
    @RequestMapping("")
    public Map<String,Object> get() {
        Map<String, Object> rm = new HashMap<>();
        rm.put("test", "贷款方");
        logger.error("测试日志");
//        LogManager.getLogger(UserController.class).error("kskksks可贷款将发动机");
        return rm;
    }

}
