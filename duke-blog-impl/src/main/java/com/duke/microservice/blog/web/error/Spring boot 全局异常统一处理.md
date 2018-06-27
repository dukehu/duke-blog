## Spring boot 全局异常统一处理

### 方案一：基于@ControllerAdvice注解的Controller层的全局异常统一处理

> 参考https://www.cnblogs.com/magicalSam/p/7198420.html

##### 创建MyControllerAdvice，并加上@ControllerAdvice注解

```$xslt
import com.duke.microservice.blog.common.Response;
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
     * @param binder  参考：https://blog.csdn.net/xsf1840/article/details/73556633
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

}
```

**应用启动后，被@ExceptionHandler、@InitBinder、@ModelAttribute注解的方法，都会作用在@RequestMapping注解的方法上。**
**@ExceptionHandler拦截了异常，可以通过该注解实现自定义的异常处理。@ExceptionHandler的value指定需要拦截的异常类型**


### 方案二：自定义异常处理（全局异常处理）

> Spring boot 默认情况下会映射到/error进行异常处理
