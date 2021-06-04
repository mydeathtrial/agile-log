package com.agile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 佟盟
 * 日期 2020/8/00018 17:30
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RunLogTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() {
        testRestTemplate.getForObject("/test1?a=12", String.class);
    }

    @Test
    public void test2() {
        testRestTemplate.getForObject("/test2?a=12", String.class);
    }

    @Autowired
    private Role2 role;
    @Test
    public void test3(){
//        //表达式解析器
//        ExpressionParser parser = new SpelExpressionParser();
//        // 设置表达式
//        Expression exp = parser.parseExpression("'hello world'");
//        String str = (String) exp.getValue();
//        System.out.println(str);
//        //通过EL访问普通方法
//        exp = parser.parseExpression("'hello world'.charAt(0)");
//        char ch = (Character) exp.getValue();
//        System.out.println(ch);
//        //通过EL访问的getter方法
//        exp = parser.parseExpression("'hello world'.bytes");
//        byte[] bytes = (byte[]) exp.getValue();
//        System.out.println(Arrays.toString(bytes));
//        //通过EL访问属性，相当于"hello world".getBytes().length
//        exp = parser.parseExpression("'hello world'.bytes.length");
//        int length = (Integer) exp.getValue();
//        System.out.println(length);
//        exp = parser.parseExpression("new String('abc')");
//        String abc = (String) exp.getValue();
//        System.out.println(abc);



        //表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        //创建角色对象
        Expression exp = parser.parseExpression("note");
        //相当于从role中获取备注信息
        String note = (String) exp.getValue(role);
        System.out.println(note);
        //变量环境类，并且将角色对象role作为其根节点
        EvaluationContext ctx = new StandardEvaluationContext(role);

        ctx.setVariable("tudou",role);
        //变量环境类操作根节点
        exp.setValue(ctx, "new_note");

        String sql = "select * from #{new_note}";

        exp.getExpressionString();
        //获取备注，这里的String.class指明，我们希望返回的是一个字符串
        note = exp.getValue(ctx, String.class);
        System.out.println(note);
        //调用getRoleName方法
        String roleName = parser.parseExpression("getRoleName()").getValue(ctx, String.class);
        System.out.println(roleName);
        //新增环境变量
        List<String> list = new ArrayList<String>();
        list.add("value1");
        list.add("value2");
        //给变量环境增加变量
        ctx.setVariable("list", list);
        //通过表达式去读/写环境变量的值
        parser.parseExpression("#list[1]").setValue(ctx, "update_value2");
        System.out.println(parser.parseExpression("#list[1]").getValue(ctx));
        System.out.println(parser.parseExpression("#list").getValue(ctx));
    }

    @Test
    public void test4(){
        //将实体类序列化成 map (HashMap允许加入null值)
        //SerializerFeature.WriteMapNullValue ：序列化策略-》允许写入null值
        HashMap<String,Object> HashMap = JSON.parseObject(JSON.toJSONString(role, SerializerFeature.WriteMapNullValue),HashMap.class);
        //自定义模板   因为正则用的{{**}}进行匹配，所以模板就写成例如:{{areaName}}
        String nameStr = "您所管理的区县中区县为{ id }，企业名为:\\{roleName\\}的企业,下次上传{xmmc}时间为{tjDate}，请尽快处理!";
        System.err.println("序列化成map的值:    "+HashMap);
        //进行解析  注意：解析的时候尽保证 map中的value为string行，不然解析会报错
        String result =  renderString(nameStr,HashMap);
        System.err.println("模板解析后的值:    "+result);
    }

    public static String renderString(String content, Map<String,Object> map){
        Set<Map.Entry<String,Object>> sets = map.entrySet();
        try{
            for(Map.Entry<String,Object> entry : sets){
                //定义正则
                String regex = "\\{" + entry.getKey() + "\\}";
                //创建匹配模式
                Pattern pattern = Pattern.compile(regex);
                //将要匹配的内容加入进行匹配的
                Matcher matcher = pattern.matcher(content);
                //将匹配的结果替换为map的value值
                content = matcher.replaceAll(entry.getValue() == null ? "null" : String.valueOf(entry.getValue()));
            }
        }catch (ClassCastException e){
            new ClassCastException("格式错误，模板解析时map都应为string类型");
            return "格式错误，模板解析时map中的value都应为string类型";
        }
        return content;
    }
}
