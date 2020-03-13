package com.qf.exception;


import com.qf.entity.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerError(HttpServletRequest request, Exception e) {
        String header = request.getHeader("X-Requested-With");
        if (header != null && header.equals("XMLHttpRequest")) {
            return new ResultData<String>().setCode(ResultData.ResultCodeList.ERROR).setMsg("服务器繁忙请稍后再试...");
        } else {
            return new ModelAndView("error");
        }
    }
}
