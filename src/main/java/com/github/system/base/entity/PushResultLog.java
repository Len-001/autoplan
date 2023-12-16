package com.github.system.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("推送结果表")
@TableName("log_push_result")
public class PushResultLog {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("任务日志id")
    private Long taskId;

    @ApiModelProperty("是否成功 0失败 1成功")
    private Integer success;

    @ApiModelProperty("推送结束时间")
    private Date date;

    @ApiModelProperty("推送结果数据")
    private String data;

}
