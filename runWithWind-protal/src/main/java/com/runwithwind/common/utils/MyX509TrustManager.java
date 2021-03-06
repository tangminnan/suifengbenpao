package com.runwithwind.common.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/** 微信推送通知的信任管理器 */
public class MyX509TrustManager implements X509TrustManager {

  /**
   * 该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这个方法。 JSSE中，默认的信任管理器类为TrustManager。
   *
   * @param x509Certificates
   * @param s
   * @throws CertificateException
   */
  @Override
  public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    // TODO Auto-generated method stub

  }

  /**
   * 该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。
   * 在实现该方法时，也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
   *
   * @param x509Certificates
   * @param s
   * @throws CertificateException
   */
  @Override
  public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    // TODO Auto-generated method stub

  }

  /**
   * 返回受信任的X509证书数组。
   *
   * @return
   */
  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }
}
