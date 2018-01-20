package com.dengpu.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.dengpu.demo.model.menu.Button;
import com.dengpu.demo.model.menu.ClickButton;
import com.dengpu.demo.model.menu.Menu;
import com.dengpu.demo.model.menu.ViewButton;
import com.dengpu.demo.service.menu.MenuService;
import com.dengpu.demo.util.WeiXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对订阅号的菜单的操作
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;


    private static Logger log = LoggerFactory.getLogger(MenuController.class);

    //查询全部菜单
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getMenu() {
        // 调用接口获取access_token
        String at = WeiXinUtil.getAccess_Token();
        JSONObject jsonObject = null;
        if (at != null) {
            // 调用接口查询菜单
            jsonObject = menuService.getMenu(at);
            // 判断菜单创建结果
            return String.valueOf(jsonObject);
        }
        log.info("token为" + at);
        return "无数据";
    }

    //创建菜单
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public int createMenu() {
        // 调用接口获取access_token
        String at = WeiXinUtil.getAccess_Token();
        int result = 0;
        if (at != null) {

            // 调用接口创建菜单
            result = menuService.createMenu(getFirstMenu(), at);
            // 判断菜单创建结果
            if (0 == result) {
                log.info("菜单创建成功！");
            } else {
                log.info("菜单创建失败，错误码：" + result);
            }
        }
        return result;
    }

    //删除菜单
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public int deleteMenu() {
        // 调用接口获取access_token
        String at = WeiXinUtil.getAccess_Token();
        int result = 0;
        if (at != null) {
            // 删除菜单
            result = menuService.deleteMenu(at);
            // 判断菜单删除结果
            if (0 == result) {
                log.info("菜单删除成功！");
            } else {
                log.info("菜单删除失败，错误码：" + result);
            }
        }
        return result;
    }


    /**
     * 组装菜单数据
     */
    public static Menu getFirstMenu() {
        String result = "";
        //创建点击一级菜单
        ClickButton button11 = new ClickButton();
        button11.setName("往期活动");
        button11.setKey("11");
        button11.setType("click");

        //创建跳转型一级菜单
        ViewButton button21 = new ViewButton();
        button21.setName("百度一下");
        button21.setType("view");
        button21.setUrl("https://www.baidu.com");

        //创建其他类型的菜单与click型用法一致
        ClickButton button31 = new ClickButton();
        button31.setName("拍照发图");
        button31.setType("pic_photo_or_album");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("发送位置");
        button32.setKey("32");
        button32.setType("location_select");

        //封装到一级菜单
        Button button = new Button();
        button.setName("菜单");
        button.setType("click");
        button.setSub_button(new Button[]{button31, button32});

        //封装菜单
        Menu menu = new Menu();
        menu.setButton(new Button[]{button11, button21, button});

        return menu;
    }
}
