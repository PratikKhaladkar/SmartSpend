package com.expense_tracker.security.jwt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CachedBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        byte[] body = httpRequest.getInputStream().readAllBytes();
        

        chain.doFilter(new CachedBodyRequestWrapper(httpRequest, body), response);
    }

    
    static class CachedBodyRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] cachedBody;

        public CachedBodyRequestWrapper(HttpServletRequest request, byte[] body) {
            super(request);
            this.cachedBody = body;
        }

        @Override
        public ServletInputStream getInputStream() {
            // Creates a FRESH ByteArrayInputStream every time it's called
            ByteArrayInputStream bis = new ByteArrayInputStream(cachedBody);
            return new ServletInputStream() {
                public int read() { return bis.read(); }
                public boolean isFinished() { return bis.available() == 0; }
                public boolean isReady() { return true; }
                public void setReadListener(ReadListener l) {}
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
        
        @Override
        public int getContentLength() {
            return cachedBody.length;
        }

        @Override
        public long getContentLengthLong() {
            return cachedBody.length;
        }

        @Override
        public String getCharacterEncoding() {
            return StandardCharsets.UTF_8.name();
        }
    }
}