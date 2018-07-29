package com.duke.microservice.blog.web.error;

import com.duke.framework.exception.BusinessException;
import com.duke.framework.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * Created pc on 2018/6/27
 */
@ControllerAdvice
public class MyControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyControllerAdvice.class);


    /**
     * 应用到所有的@RequestMapping注解的方法，在其执行之前初始化数据绑定
     *
     * @param binder 参考：https://blog.csdn.net/xsf1840/article/details/73556633
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 将值绑定到model中，使全局的@RequestMapping可以获取到该值
     *
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "duke");
    }

    /**
     * 全局异常捕获处理
     *
     * @param ex 异常对象
     * @return map
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(Exception ex) {
        LOGGER.error("Exception: status[{}], code[{}], message[{}], error[{}]", "", "", ex.getMessage(), ex);
        return Response.error(10000, "", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Response errorBusinessExceptionHandler(BusinessException ex) {
        LOGGER.error("Exception: status[{}], code[{}], message[{}], error{}", ex.getStatus(), ex.getCode(), ex.getMessage(), ex.getFieldErrors().toString(), ex);
        return Response.error(ex.getFieldErrors());
    }

}
