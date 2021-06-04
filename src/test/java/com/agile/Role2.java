package com.agile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 佟盟
 * 日期 2021-05-12 13:45
 * 描述 TODO
 * @version 1.0
 * @since 1.0
 */
@Component("role2")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role2 {

    //赋值long型
    @Value("#{4444}")
    private Long id;
    //字符串赋值
    @Value("#{'role_name_2'}")
    private String roleName;
    //字符串赋值
    @Value("#{'note_2'}")
    private String note;

    //字符串赋值
    @Value("#{T(Math).random()}")
    private String random;
}
