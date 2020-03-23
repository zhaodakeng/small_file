package com.example.file.config;


import com.example.file.dao.AccessDao;
import com.example.file.model.AccessModel;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


public class LogCostInterceptor implements HandlerInterceptor {
    long start = System.currentTimeMillis();
    //编码格式。发送编码格式统一用UTF-8
    private static String ENCODING = "UTF-8";

    private <T> T getDAO(Class<T> clazz, HttpServletRequest request) {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }

    // 在业务处理器处理请求之前被调用
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String url = httpServletRequest.getHeader("Referer");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要密钥
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation == null) {
            return true;
        }
        AccessDao accessDao = getDAO(AccessDao.class, httpServletRequest);
        String accessKey = httpServletRequest.getHeader("AccessKey");
        AccessModel accessModel = accessDao.findByAccessKey(accessKey);
        if (null == accessModel) {
            throw new CommonException(500, "AccessKey异常");
        }
        List<String> strUrl = Arrays.asList(accessModel.getUrl().split(","));
        if (strUrl.indexOf(url) > -1) {
            return true;
        } else {
            throw new CommonException(500, "不在允许的域名中");
        }
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     * 验证token是否可用
     *
     * @param url           提交的URL
     * @param Authorization 提交<参数，值>Map
     * @return 提交响应
     */

    public static String post(String url, String Authorization) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            method.setHeader("Authorization", Authorization);
//            Authorization="{\"token\":\""+Authorization+"\"}";
            method.setEntity(new ByteArrayEntity(Authorization.getBytes("UTF-8")));
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("Interceptor cost=" + (System.currentTimeMillis() - start));
    }

    // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}