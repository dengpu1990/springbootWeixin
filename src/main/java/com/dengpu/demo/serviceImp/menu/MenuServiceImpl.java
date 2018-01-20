package com.dengpu.demo.serviceImp.menu;

import com.alibaba.fastjson.JSONObject;
import com.dengpu.demo.model.menu.Menu;
import com.dengpu.demo.service.menu.MenuService;
import com.dengpu.demo.util.WeiXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Title: XXXX (类或者接口名称)
 * Description: XXXX (简单对此类或接口的名字进行描述)
 * Copyright: Copyright (c) 2008
 * Company:深圳亿起融网络科技有限公司
 *
 * @author dengpu on 2018/1/20.
 * @version 1.0
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService{

    private static Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    // 菜单创建（POST） 限1000（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    // 菜单查询（POST） 限10000（次/天）
    public static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    // 菜单删除（POST） 限1000（次/天）
    public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";


    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return
     */
    @Override
    public JSONObject getMenu(String accessToken) {

        // 拼装创建菜单的url
        String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = WeiXinUtil.httpsRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 创建菜单(替换旧菜单)
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int createMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.toJSONString(menu);
        // 调用接口创建菜单
        JSONObject jsonObject = WeiXinUtil.httpsRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
                log.error("****"+jsonMenu+"****");
            }
        }
        return result;
    }


    /**
     * 删除菜单
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int deleteMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = WeiXinUtil.httpsRequest(url, "POST", null);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }
}
