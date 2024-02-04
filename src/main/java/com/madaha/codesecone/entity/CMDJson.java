package com.madaha.codesecone.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("amis-fastjson-springboot测试")     // swagger3
@Data       // lombok配置
@NoArgsConstructor
@AllArgsConstructor
public class CMDJson {
    @JSONField(name = "cmd_json")             // json传输，“前端”值转换；
    public String cmd;
    @JSONField(name = "last_return_value")    // json传输，“后端”值转换；
    public String returnValue;
}
