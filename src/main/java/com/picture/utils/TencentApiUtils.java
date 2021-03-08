package com.picture.utils;

import com.alibaba.fastjson.JSON;
import com.picture.utils.result.IdentifyImageResult;
import com.springboot.simple.support.util.RandomUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.*;

public class TencentApiUtils {
    private static final String API_ADDRESS = "https://api.ai.qq.com/fcgi-bin/image/image_tag";
    private static final Long APP_ID = 2166188829L;
    private static final String APP_KEY = "Gaqg46cYnxHD1AXH";

    /**
     * 通过文件路径解析
     * @param imagePath
     * @return
     */
    public static IdentifyImageResult identifyImage(String imagePath) {
        return identifyImage(new File(imagePath));
    }

    /**
     * 通过文件对象解析
     * @param file
     * @return
     */
    public static IdentifyImageResult identifyImage(File file) {
        return identifyImage(fileToBase64(file));
    }

    /**
     * 通过文件的字节数组解析
     * @param bytes
     * @return
     */
    public static IdentifyImageResult identifyImage(byte[] bytes) {
        final Map<String, String> params = new HashMap<>();
        params.put("app_id", APP_ID.toString());
        params.put("time_stamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("nonce_str", RandomUtils.randomStr(15));
        params.put("image", Base64Util.encode(bytes));
        String resultStr = doIdentifyImage(params);
        return JSON.parseObject(resultStr, IdentifyImageResult.class);
    }


    /**
     * 发起识别图片的请求
     * @param params
     * @return
     */
    private static String doIdentifyImage(Map<String, String> params) {
        String sign;
        try {
            sign = getSign(params);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        params.put("sign", sign);
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            data.append(entry.getKey())
                    .append("=");
            try {
                data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            data.append("&");
        }

        data.deleteCharAt(data.length() - 1);

        try {
            byte[] postDataBytes = data.toString().getBytes(StandardCharsets.UTF_8);
            HttpURLConnection conn = (HttpURLConnection) new URL(API_ADDRESS).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            conn.connect();

            StringBuilder sb = new StringBuilder();
            if (conn.getResponseCode() == 200) {
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                for (int c; (c = in.read()) >= 0;) {
                    sb.append((char) c);
                }
                in.close();
            } else {
                sb.append("错误码 ：")
                        .append(conn.getResponseCode())
                        .append(" 错误信息 ：")
                        .append(conn.getResponseMessage());
            }

            conn.disconnect();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 文件base64编码
     * @param file
     * @return
     */
    private static byte[] fileToBase64(File file) {
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(file.toPath());
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 构建鉴权字符串
     * @param params
     * @return
     * @throws Exception
     */
    private static String getSign(Map<String, String> params) throws Exception {
        final List<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keys.add(entry.getKey());
        }
        keys.sort(String.CASE_INSENSITIVE_ORDER);
        StringBuilder data = new StringBuilder();
        for (String key : keys) {
            data.append(key)
                    .append("=")
                    .append(URLEncoder.encode(params.get(key), "UTF-8"))
                    .append("&");
        }

        data.append("app_key=")
                .append(APP_KEY);
        return Objects.requireNonNull(getMD5(data.toString())).toUpperCase();
    }

    /**
     * 计算MD5摘要指值
     * @param s
     * @return String
     */
    private static String getMD5(String s) {
        char[] hexDigits ={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
