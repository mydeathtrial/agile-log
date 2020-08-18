package com.agile;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 佟盟
 * 日期 2020/8/00017 14:25
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
@Controller
public class TestController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @RequestMapping("/test1")
    public WebAsyncTask<ModelAndView> test1(int a) {
        return new WebAsyncTask<>(30000, ()->{
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(new FastJsonJsonView());
            modelAndView.addObject("a","输出");
            if(a==1){
                RuntimeException e = new RuntimeException("错了");
                logger.error("出错了",e);
                throw e;
            }
            logger.info("访问了1");
            logger.error("我是错误日志");
            return modelAndView;
        });
    }

    @ResponseBody
    @RequestMapping("/test2")
    public ModelAndView test2(int a) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new FastJsonJsonView());
        modelAndView.addObject("a","输出");
        logger.info("访问了2");
        logger.error("我是错误日志");
        return modelAndView;
    }
}
