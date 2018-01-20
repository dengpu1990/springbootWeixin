package com.dengpu.demo.controller;

import com.dengpu.demo.service.core.CoreService;
import com.dengpu.demo.util.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Title: XXXX (类或者接口名称)
 * Description: XXXX (简单对此类或接口的名字进行描述)
 * Copyright: Copyright (c) 2008
 * Company:深圳亿起融网络科技有限公司
 *
 * @author dengpu on 2018/1/20.
 * @version 1.0
 */
@RestController
//@RequestMapping("")
public class CoreController {

    private static Logger log = LoggerFactory.getLogger(CoreController.class);

    @Autowired
    private CoreService coreService;

    @GetMapping(value = "hello",produces = "text/plain;charset=utf-8")
    public String login(@RequestParam(name = "signature" ,required = false) String signature  ,
                      @RequestParam(name = "timestamp" ,required = false) String timestamp  ,
                      @RequestParam(name = "nonce",required = false) String  nonce ,
                      @RequestParam(name = "echostr",required = false) String  echostr) {
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            log.info("接入成功");
            return echostr;
        }
        log.error("接入失败");
        return null;
    }

    @PostMapping(value = "hello")
    public String post(HttpServletRequest req){
        log.info("接受到请求");
        String respMessage = coreService.processRequest(req);
        return respMessage;
    }
}
