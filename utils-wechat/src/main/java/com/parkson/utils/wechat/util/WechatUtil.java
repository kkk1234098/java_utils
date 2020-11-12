package com.parkson.utils.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.parkson.utils.core.util.JsonUtil;
import com.parkson.utils.http.util.HttpUtil;
import com.parkson.utils.wechat.entity.WechatAccessTokenResponse;
import com.parkson.utils.wechat.entity.WechatSessionResponse;
import com.parkson.utils.wechat.entity.WechatUserInfoResponse;
import com.parkson.utils.wechat.entity.WechatUserListResponse;
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
    public static final String URL_GET_OFFICIAL_USER_LIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPEN_ID";
    public static final String URL_GET_OFFICIAL_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPEN_ID";

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
        WechatSessionResponse response = new WechatSessionResponse();
        response.setErrCode(jsonObject.getInteger("errcode"));
        response.setErrMsg(jsonObject.getString("errmsg"));
        // 成功时errcode字段不存在
        if (response.getErrCode() == null) {
            response.setSessionKey(jsonObject.getString("session_key"));
            response.setOpenId(jsonObject.getString("openid"));
            response.setUnionId(jsonObject.getString("unionid"));
        }
        return response;
    }

    /**
     * 用sessionKey解密encryptedData，可获取手机号或者用户信息
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public static JSONObject decryptData(String sessionKey, String encryptedData, String iv) throws Exception {
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
        response.setErrCode(jsonObject.getInteger("errcode"));
        response.setErrMsg(jsonObject.getString("errmsg"));
        // 成功时errcode字段不存在
        if (response.getErrCode() == null) {
            response.setAccessToken(jsonObject.getString("access_token"));
            response.setExpiresIn(jsonObject.getInteger("expires_in"));
        }
        return response;
    }

    /**
     * 获取公众号的关注用户列表
     * @param accessToken
     * @param nextOpenID
     * @return
     * @throws Exception
     */
    public static WechatUserListResponse getOfficialUserList(String accessToken, String nextOpenID) throws Exception {
        String requestUrl = URL_GET_OFFICIAL_USER_LIST.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPEN_ID", nextOpenID != null ? nextOpenID : "");
        String ret = HttpUtil.doGet(requestUrl);
        JSONObject jsonObject = JsonUtil.toJavaObject(ret, JSONObject.class);
        WechatUserListResponse response = new WechatUserListResponse();
        response.setErrCode(jsonObject.getInteger("errcode"));
        response.setErrMsg(jsonObject.getString("errmsg"));
        // 成功时errcode字段不存在
        if (response.getErrCode() == null) {
            response.setTotal(jsonObject.getLong("total"));
            response.setCount(jsonObject.getLong("count"));
            response.setNextOpenId(jsonObject.getString("next_openid"));
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                response.setOpenIdList(JSON.parseArray(data.getString("openid"), String.class));
            }
            response.setTotal(jsonObject.getLong("total"));
        }

        return response;
    }

    /**
     * 获取公众号的用户详情
     * @param accessToken
     * @param openId
     * @return
     * @throws Exception
     */
    public static WechatUserInfoResponse getOfficialUserInfo(String accessToken, String openId) throws Exception {
        String requestUrl = URL_GET_OFFICIAL_USER_INFO.replace("ACCESS_TOKEN", accessToken).replace("OPEN_ID", openId);
        String ret = HttpUtil.doGet(requestUrl);
        JSONObject jsonObject = JsonUtil.toJavaObject(ret, JSONObject.class);
        WechatUserInfoResponse response = new WechatUserInfoResponse();
        response.setErrCode(jsonObject.getInteger("errcode"));
        response.setErrMsg(jsonObject.getString("errmsg"));
        // 成功时errcode字段不存在
        if (response.getErrCode() == null) {
            response.setOpenId(jsonObject.getString("openid"));
            response.setUnionId(jsonObject.getString("unionid"));
            response.setNickname(jsonObject.getString("nickname"));
            response.setSubscribe(jsonObject.getBoolean("subscribe"));
        }

        return response;
    }
}