package com.dengpu.demo.service.menu;

import com.alibaba.fastjson.JSONObject;
import com.dengpu.demo.model.menu.Menu;

/**
 * Title: XXXX (类或者接口名称)
 * Description: XXXX (简单对此类或接口的名字进行描述)
 * Copyright: Copyright (c) 2008
 * Company:深圳亿起融网络科技有限公司
 *
 * @author dengpu on 2018/1/20.
 * @version 1.0
 */
public interface MenuService {
    public JSONObject getMenu(String accessToken);
    public int createMenu(Menu menu, String accessToken);
    public int deleteMenu(String accessToken);
}
