package com.dengpu.demo.model.message.req;

/**
 * 文本消息
 *
 */
public class TextMessageReq extends BaseMessageReq {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
