package com.parkson.utils.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.parkson.utils.wechat.entity.WechatAccessTokenResponse;
import com.parkson.utils.wechat.entity.WechatSessionResponse;
import com.parkson.utils.core.util.JsonUtil;
import com.parkson.utils.http.util.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 * @ClassName WechatUtil
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/7/21 16:41
 * @Version 1.0
 */
public class WechatUtil {
    public static final String AES = "AES";
    public static final String AES_CBC_PADDING = "AES/CBC/PKCS7Padding";
    public static final String URL_JSCODE_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
    public static final String URL_GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

    /**
     * code换session
     * @param appId
     * @param appSecret
     * @param code
     * @return
     * @throws Exception
     */
    public static WechatSessionResponse code2Session(String appId, String appSecret, String code) throws Exception {
        String requestUrl = URL_JSCODE_SESSION.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        String ret = HttpUtil.doGet(requestUrl);
        JSONObject jsonObject = JsonUtil.toJavaObject(ret, JSONObject.class);
        WechatSessionResponse session = new WechatSessionResponse();
        session.setSessionKey(jsonObject.getString("session_key"));
        session.setOpenId(jsonObject.getString("openid"));
        session.setErrCode(jsonObject.getInteger("errcode"));
        session.setErrMsg(jsonObject.getString("errmsg"));
        return session;
    }

    /**
     * 获取手机号
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public static JSONObject getPhoneNumber(String sessionKey, String encryptedData, String iv) {
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        byte[] ivByte = Base64.decodeBase64(iv);

        try {
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(WechatUtil.AES_CBC_PADDING);
            SecretKeySpec spec = new SecretKeySpec(keyByte, WechatUtil.AES);
            AlgorithmParameters parameters = AlgorithmParameters.getInstance(WechatUtil.AES);
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app的AccessToken
     * @param appId
     * @param appSecret
     * @return
     */
    public static WechatAccessTokenResponse getAccessToken(String appId, String appSecret) throws Exception {
        String requestUrl = URL_GET_ACCESS_TOKEN.replace("APPID", appId).replace("SECRET", appSecret);
        String ret = HttpUtil.doGet(requestUrl);
        JSONObject jsonObject = JsonUtil.toJavaObject(ret, JSONObject.class);
        WechatAccessTokenResponse response = new WechatAccessTokenResponse();
        response.setAccessToken(jsonObject.getString("access_token"));
        response.setExpiresIn(jsonObject.getInteger("expires_in"));
        response.setErrCode(jsonObject.getInteger("errcode"));
        response.setErrMsg(jsonObject.getString("errmsg"));
        return response;
    }
}