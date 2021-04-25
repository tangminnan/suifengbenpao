package com.runwithwind.framework.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.runwithwind.common.utils.AccessTokenAndNextOpenId;
import com.runwithwind.common.utils.GZHMsgVo;
import com.runwithwind.common.utils.MyX509TrustManager;
import com.runwithwind.common.utils.WxTuiSongUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WechatOAConfig {

  public static final String APP_ID = "wxe23f18d7b8947aa6";
  public static final String secret = "cc4f11939cd0fe3e18201daf13e69ce6";
  public static final String TEMPLATE_ID = "Z3tc6y64gC71teATxD4iHrLL2MP-w5Owucra2AcBec4";

  public static final String SM_URL = "http://qinzi.jingtu99.com/baogao.html?id=";

  private static Logger log = LoggerFactory.getLogger(WxTuiSongUtils.class);

  private static Logger logger = LoggerFactory.getLogger(WechatOAConfig.class);

  public static Map<String, String> getUserInfo(String code) throws Exception {
    String openid = "";
    String access_token = "";
    String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
    String para =
        "appid=wxe23f18d7b8947aa6&secret=cc4f11939cd0fe3e18201daf13e69ce6&code="
            + code
            + "&grant_type=authorization_code";
    String res = sendGet(url, para);
    Map<String, String> map = new HashMap<String, String>();
    Map<String, String> userMap = new HashMap<>();
    map = JSON.parseObject(res, Map.class);
    openid = map.get("openid");
    access_token = map.get("access_token");

    if (openid != null && access_token != null) {
      String userifon =
          sendGet(
              " https://api.weixin.qq.com/sns/userinfo",
              "access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN");
      userMap = JSON.parseObject(userifon, Map.class);
    }

    return userMap;
  }

  public static String sendGet(String url, String param) {
    String result = "";
    BufferedReader in = null;
    try {
      String urlNameString = url + "?" + param;
      URL realUrl = new URL(urlNameString);
      // 打开和URL之间的连接
      URLConnection connection = realUrl.openConnection();
      // 设置通用的请求属性
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty(
          "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // 建立实际的连接
      connection.connect();
      // 获取所有响应头字段
      Map<String, List<String>> map = connection.getHeaderFields();
      // 遍历所有的响应头字段
      for (String key : map.keySet()) {
        System.out.println(key + "--->" + map.get(key));
      }
      // 定义 BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      System.out.println("发送GET请求出现异常！" + e);
      e.printStackTrace();
    }
    // 使用finally块来关闭输入流
    finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return result;
  }
  /** 获取接口调用凭证 */
  public static AccessTokenAndNextOpenId getAccessToken() {
    String access_tokenurl =
        "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
            + APP_ID
            + "&secret="
            + secret;
    String accesstoken = "";
    String nextopenid = "";
    JSONObject jsonObject = httpsRequest(access_tokenurl, "GET", null);

    if (jsonObject != null) {
      accesstoken = jsonObject.getString("access_token");
    }
    AccessTokenAndNextOpenId accessTokenAndNextOpenId = new AccessTokenAndNextOpenId();
    accessTokenAndNextOpenId.setAccessToken(accesstoken);
    accessTokenAndNextOpenId.setNextOpenId("");
    return accessTokenAndNextOpenId;
  }

  /** 封装消息模板 */
  public static void sendMessagePush(List<GZHMsgVo> voList, String ACCESS_TOKEN) {
    for (int i = 0; i < voList.size(); i++) {
      GZHMsgVo vo = voList.get(i);
      JSONObject json = new JSONObject();
      JSONObject text = new JSONObject();
      JSONObject first = new JSONObject();
      JSONObject remark = new JSONObject();
      json.put("touser", vo.getTouser()); // 微信用户的openId
      json.put("template_id", vo.getTemplateId()); // 消息模板id
      json.put("url", vo.getSmUrl()); // 筛查报告详情页路径
      first.put("value", vo.getFirst()); // 标题
      first.put("color", vo.getTitleColor()); // 标题颜色
      remark.put("value", vo.getRemark()); // 备注
      remark.put("color", vo.getRemakColor()); // 备注字体颜色
      JSONObject keyword1 = new JSONObject();
      keyword1.put("value", vo.getKeyword1());
      text.put("keyword1", keyword1);

      JSONObject keyword2 = new JSONObject();
      keyword2.put("value", vo.getKeyword2());
      text.put("keyword2", keyword2);
      JSONObject keyword3 = new JSONObject();
      keyword3.put("value", vo.getKeyword3());
      text.put("keyword3", keyword3);
      JSONObject keyword4 = new JSONObject();
      keyword4.put("value", vo.getKeyword4());
      text.put("keyword4", keyword4);

      text.put("first", first);
      text.put("remark", remark);
      json.put("data", text);
      // 发送模板消息
      String sendUrl =
          "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
      sendUrl = sendUrl.replace("ACCESS_TOKEN", ACCESS_TOKEN);
      String jsonstringParm = json.toString();
      System.out.println("-------------" + jsonstringParm);
      System.out.println("-------------" + jsonstringParm);
      System.out.println("-------------" + jsonstringParm);
      System.out.println("-------------" + jsonstringParm);
      JSONObject object = httpsRequest(sendUrl, "POST", jsonstringParm);
      System.out.println("消息推送成功==========================" + object.toJSONString());
    }
  }

  /**
   * 发送https请求
   *
   * @param requestUrl 请求地址
   * @param requestMethod 请求方式（GET、POST）
   * @param outputStr 提交的数据
   * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
   */
  public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
    JSONObject jsonObject = null;
    try {
      // 创建SSLContext对象，并使用我们指定的信任管理器初始化
      TrustManager[] tm = {new MyX509TrustManager()};
      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new java.security.SecureRandom());
      // 从上述SSLContext对象中得到SSLSocketFactory对象
      SSLSocketFactory ssf = sslContext.getSocketFactory();

      URL url = new URL(requestUrl);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setSSLSocketFactory(ssf);

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      // 设置请求方式（GET/POST）
      conn.setRequestMethod(requestMethod);

      // 当outputStr不为null时向输出流写数据
      if (null != outputStr) {
        OutputStream outputStream = conn.getOutputStream();
        // 注意编码格式
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }

      // 从输入流读取返回内容
      InputStream inputStream = conn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String str = null;
      StringBuffer buffer = new StringBuffer();
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }

      // 释放资源
      bufferedReader.close();
      inputStreamReader.close();
      inputStream.close();
      conn.disconnect();
      jsonObject = JSONObject.parseObject(buffer.toString());
    } catch (ConnectException ce) {

      log.error("连接超时：{}", ce);
    } catch (Exception e) {
      log.error("https请求异常：{}", e);
    }
    return jsonObject;
  }

  public static String getJsapiTicket(String access_token) {
    String ticket = null;
    // String accessToken = getAccessToken();
    String uri =
        "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
            + access_token
            + "&type=jsapi";
    try {
      URL urlGet = new URL(uri);
      HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
      http.setRequestMethod("GET"); // 必须是get方式请求
      http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      http.setDoOutput(true);
      http.setDoInput(true);
      System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); // 连接超时30秒
      System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
      http.connect();
      InputStream is = http.getInputStream();
      int size = is.available();
      byte[] jsonBytes = new byte[size];
      is.read(jsonBytes);
      String message = new String(jsonBytes, "UTF-8");
      JSONObject demoJson = JSON.parseObject(message);
      ticket = demoJson.getString("ticket");
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ticket;
  }
}
