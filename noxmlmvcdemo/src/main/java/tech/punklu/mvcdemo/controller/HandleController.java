package tech.punklu.mvcdemo.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 相当于定义了uri为/test1.do的web接口
 */
@Component("/test1.do") //BeanName
public class HandleController implements HttpRequestHandler {



    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("111");
    }
}
