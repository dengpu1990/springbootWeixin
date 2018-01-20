package com.dengpu.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.dengpu.demo.entity.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;

/** 
 *  
 * 类名称: WeiXinUtil 
 * 类描述: 
 * @author yuanjun 
 * 创建时间:2017年12月8日下午4:38:42 
 */  
public class WeiXinUtil {

    private static Logger log = LoggerFactory.getLogger(WeiXinUtil.class);
    /** 
     * 开发者id 
     */  
    private static final String APPID = "wx9c6a844a4b49cd96";  
    /** 
     * 开发者秘钥 
     */  
    private static final String APPSECRET="cffbb9f3854d289e843f878c0b0f9f5c";  
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?"  
            + "grant_type=client_credential&appid=APPID&secret=APPSECRET";  
    /** 
     * 处理doget请求 
     * @param url 
     * @return 
     */  
    public static JSONObject doGetstr(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpGet httpGet = new HttpGet(url);  
        JSONObject jsonObject = null;  
        try {  
            CloseableHttpResponse response = httpclient.execute(httpGet);  
            HttpEntity entity = response.getEntity();  
            if(entity!=null){  
                String result = EntityUtils.toString(entity);  
                jsonObject = JSONObject.parseObject(result);
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return jsonObject;  
          
    }  
    /** 
     * 处理post请求 
     * @param url 
     * @return 
     */  
    public static JSONObject doPoststr(String url,String outStr){  
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpPost httpPost = new HttpPost(url);  
        JSONObject jsonObject = null;  
        try {  
            httpPost.setEntity(new StringEntity(outStr, "utf-8"));  
            CloseableHttpResponse response = httpclient.execute(httpPost);  
            String result = EntityUtils.toString(response.getEntity(),"utf-8");  
            jsonObject =JSONObject.parseObject(result);
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return jsonObject;  
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)){
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    public static AccessToken getAccessToken(){  
        System.out.println("从接口中获取");  
        Jedis jedis  = RedisUtil.getJedis();  
        AccessToken token = new AccessToken();  
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);  
        JSONObject json = doGetstr(url);  
        if(json!=null){  
            token.setAccess_token(json.getString("access_token"));  
            token.setExpires_in(json.getInteger("expires_in"));
            jedis.set("access_token", json.getString("access_token"));  
            jedis.expire("access_token", 60*60*2);  
        }  
        RedisUtil.returnResource(jedis);  
        return token;  
    }  
    /** 
     * 获取凭证 
     * @return 
     */  
    public static String  getAccess_Token(){  
        System.out.println("从缓存中读取");  
        Jedis jedis  = RedisUtil.getJedis();  
        String access_token = jedis.get("access_token");  
        if(access_token==null){  
            AccessToken token = getAccessToken();  
            access_token = token.getAccess_token();  
        }  
        RedisUtil.returnResource(jedis);  
        return access_token;  
    }  
      
}  
