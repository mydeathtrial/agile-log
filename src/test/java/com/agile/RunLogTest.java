package com.agile;

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
import java.util.List;

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
    public void test3() {
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

        ctx.setVariable("tudou", role);
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
}
