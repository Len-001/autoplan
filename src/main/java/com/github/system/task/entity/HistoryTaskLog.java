package com.github.system.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("log_hi_task")
@ApiModel("历史任务日志")
public class HistoryTaskLog implements Serializable {

    @ApiModelProperty("主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("任务类型")
    private String type;

    @ApiModelProperty("任务状态")
    private Integer status;

    @ApiModelProperty("任务所属的用户id")
    private Long userid;

    @ApiModelProperty("记录时间")
    private Date date;

    @ApiModelProperty("日志内容")
    private String text;


    public HistoryTaskLog(Long taskId, String type, Integer status, Long userid, Date date, String text) {
        this.taskId = taskId;
        this.type = type;
        this.status = status;
        this.userid = userid;
        this.date = date;
        this.text = text;
    }

    public HistoryTaskLog() {
    }

}