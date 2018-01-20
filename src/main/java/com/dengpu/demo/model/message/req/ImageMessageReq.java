package com.dengpu.demo.model.message.req;

/**
 * Title: 图片消息
 * Copyright: Copyright (c) 2008
 * Company:深圳亿起融网络科技有限公司
 *
 * @author dengpu on 2018/1/20.
 * @version 1.0
 */
public class ImageMessageReq extends BaseMessageReq{
    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
