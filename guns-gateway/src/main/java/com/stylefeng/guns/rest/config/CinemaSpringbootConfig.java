//package com.stylefeng.guns.rest.config;
//
//import org.hibernate.validator.HibernateValidator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.Validator;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CinemaSpringbootConfig implements WebMvcConfigurer {
//    /**
//     * 把校验器注册到MVC容器中
//     * @return
//     */
//    @Override
//    public Validator getValidator() {
//        return validator();
//    }
//
//    /**
//     * 注册校验器
//     * @return
//     */
//    @Bean
//    public LocalValidatorFactoryBean validator(){
//        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
//        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
//        return localValidatorFactoryBean;
//    }
//
//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor(){
//        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
//        methodValidationPostProcessor.setValidator(validator());
//        return methodValidationPostProcessor;
//    }
//}
