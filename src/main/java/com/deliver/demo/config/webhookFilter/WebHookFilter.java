package com.deliver.demo.config.webhookFilter;

import com.deliver.demo.entity.WebhookRequest;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 拦截相应请求，修改请求路由
 * 当进入url为/webhook时，将其url修改为/webhook/action
 * 其中 action 为 ngd 设置的 requestBody 的接口分发参数
 **/
public class WebHookFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebHookFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }
        LOGGER.info("original webHook request url :" + httpRequest.getRequestURI());
        String path = httpRequest.getRequestURI();
        Gson gson = new Gson();
        String body = requestWrapper.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        WebhookRequest requestBody = gson.fromJson(body, WebhookRequest.class);

        String action = requestBody.getAction();
        if (StringUtils.isNoneBlank(action)) {
            String convertPath = path + "/" + action;
            LOGGER.info("webHook request url after convert :" + convertPath);
            requestWrapper.getRequestDispatcher(convertPath).forward(requestWrapper, response);
        } else {
            LOGGER.error("webHook request need a valid action param ");
        }
        return;
    }

    static class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = toByteArray(request.getInputStream());
        }

        private byte[] toByteArray(InputStream in) throws IOException {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }


        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {

                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }
    }
}