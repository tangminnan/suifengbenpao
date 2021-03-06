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
      // ?????????URL???????????????
      URLConnection connection = realUrl.openConnection();
      // ???????????????????????????
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty(
          "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // ?????????????????????
      connection.connect();
      // ???????????????????????????
      Map<String, List<String>> map = connection.getHeaderFields();
      // ??????????????????????????????
      for (String key : map.keySet()) {
        System.out.println(key + "--->" + map.get(key));
      }
      // ?????? BufferedReader??????????????????URL?????????
      in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    } catch (Exception e) {
      System.out.println("??????GET?????????????????????" + e);
      e.printStackTrace();
    }
    // ??????finally?????????????????????
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
  /** ???????????????????????? */
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

  /** ?????????????????? */
  public static void sendMessagePush(List<GZHMsgVo> voList, String ACCESS_TOKEN) {
    for (int i = 0; i < voList.size(); i++) {
      GZHMsgVo vo = voList.get(i);
      JSONObject json = new JSONObject();
      JSONObject text = new JSONObject();
      JSONObject first = new JSONObject();
      JSONObject remark = new JSONObject();
      json.put("touser", vo.getTouser()); // ???????????????openId
      json.put("template_id", vo.getTemplateId()); // ????????????id
      json.put("url", vo.getSmUrl()); // ???????????????????????????
      first.put("value", vo.getFirst()); // ??????
      first.put("color", vo.getTitleColor()); // ????????????
      remark.put("value", vo.getRemark()); // ??????
      remark.put("color", vo.getRemakColor()); // ??????????????????
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
      // ??????????????????
      String sendUrl =
          "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
      sendUrl = sendUrl.replace("ACCESS_TOKEN", ACCESS_TOKEN);
      String jsonstringParm = json.toString();
      System.out.println("-------------" + jsonstringParm);
      System.out.println("-------------" + jsonstringParm);
      System.out.println("-------------" + jsonstringParm);
      System.out.println("-------------" + jsonstringParm);
      JSONObject object = httpsRequest(sendUrl, "POST", jsonstringParm);
      System.out.println("??????????????????==========================" + object.toJSONString());
    }
  }

  /**
   * ??????https??????
   *
   * @param requestUrl ????????????
   * @param requestMethod ???????????????GET???POST???
   * @param outputStr ???????????????
   * @return JSONObject(??????JSONObject.get(key)???????????????json??????????????????)
   */
  public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
    JSONObject jsonObject = null;
    try {
      // ??????SSLContext?????????????????????????????????????????????????????????
      TrustManager[] tm = {new MyX509TrustManager()};
      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new java.security.SecureRandom());
      // ?????????SSLContext???????????????SSLSocketFactory??????
      SSLSocketFactory ssf = sslContext.getSocketFactory();

      URL url = new URL(requestUrl);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setSSLSocketFactory(ssf);

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      // ?????????????????????GET/POST???
      conn.setRequestMethod(requestMethod);

      // ???outputStr??????null????????????????????????
      if (null != outputStr) {
        OutputStream outputStream = conn.getOutputStream();
        // ??????????????????
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }

      // ??????????????????????????????
      InputStream inputStream = conn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String str = null;
      StringBuffer buffer = new StringBuffer();
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }

      // ????????????
      bufferedReader.close();
      inputStreamReader.close();
      inputStream.close();
      conn.disconnect();
      jsonObject = JSONObject.parseObject(buffer.toString());
    } catch (ConnectException ce) {

      log.error("???????????????{}", ce);
    } catch (Exception e) {
      log.error("https???????????????{}", e);
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
      http.setRequestMethod("GET"); // ?????????get????????????
      http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      http.setDoOutput(true);
      http.setDoInput(true);
      System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); // ????????????30???
      System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // ????????????30???
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
