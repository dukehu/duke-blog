package com.duke.microservice.blog.utils;

import com.duke.microservice.blog.common.FieldError;
import com.duke.microservice.blog.exception.BusinessException;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created duke on 2018/6/28
 */
public class ValidationUtils {

    private final static Logger LOG = LoggerFactory.getLogger(ValidationUtils.class);

    /**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    public static final void notEmpty(Object target, String fieldName, String message) {
        if (ObjectUtils.isEmpty(target)) {
            throw new BusinessException(new FieldError(StringUtils.isEmpty(fieldName) ? "" : fieldName, message == null ? "" : message));
        }
    }

    /**
     * 校验对象
     *
     * @param obj      验证的对象
     * @param beanName 实体名称
     * @param message  提示信息
     */
    public static <T> void validate(T obj, String beanName, String message) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            List<FieldError> filedErrors = new ArrayList<>();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                Path path = constraintViolation.getPropertyPath();
                // 属性
                String propertyName = path.toString();
                Object objValue = constraintViolation.getInvalidValue();
                String value = ObjectUtils.isEmpty(objValue) ? null : objValue.toString();
                String errMsg = constraintViolation.getMessage();
                FieldError filedError = new FieldError(propertyName, errMsg);
                filedErrors.add(filedError);
            }
            throw new BusinessException(message, filedErrors);
        }
    }
}
