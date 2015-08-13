package com.yzqc.support.handler;

import com.yzqc.support.exception.ConfilctException;
import com.yzqc.support.exception.ValidateException;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理公共类
 * <p/>
 * Created by Administrator on 2015/8/10.
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {

        //自定义数据校验
        if (e instanceof ValidateException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ModelAndView(request.getRequestURI(), "errorMsg", ((ValidateException) e).getValidationMap());
        }

        //数据不存在错误
        if (e instanceof NotFoundException) {
            //设置返回的http 状态码
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return new ModelAndView(request.getRequestURI(), "errorMsg", e.getMessage());
        }

        //数据被修改的错误
        if (e instanceof ConfilctException) {
            response.setStatus(HttpStatus.CONFLICT.value());
            return new ModelAndView(request.getRequestURI(), "errorMsg", e.getMessage());
        }

        //通用错误处理
        logger.error("服务端错误", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("error", "服务端错误");
        return new ModelAndView("error/500", "errorMsg", resultMap);
    }
}
