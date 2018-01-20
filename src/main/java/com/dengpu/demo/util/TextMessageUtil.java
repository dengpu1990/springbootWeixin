package com.dengpu.demo.util;

import com.dengpu.demo.msg.MessageText;
import com.thoughtworks.xstream.XStream;

public class TextMessageUtil implements BaseMessageUtil<MessageText>{
	/**
	 * 将发送消息封装成对应的xml格式
	 */
	@Override
	public String messageToxml(MessageText message) {
		XStream xstream = new XStream();
		xstream.alias("xml", message.getClass());
		return xstream.toXML(message);
	}


    //添加封装发送消息的方法，重载，将内容传入
	@Override
    public String initMessage(String fromUserName, String toUserName,String Content) {
        MessageText text = new MessageText();  
        text.setToUserName(fromUserName);
        text.setFromUserName(toUserName);
        text.setContent("您输入的内容是："+Content);  
        text.setCreateTime(System.currentTimeMillis());
        text.setMsgType("text");  
        return  messageToxml(text);  
    } 
}
