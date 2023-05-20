package com.powerManager.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("sys_mail")
@ApiModel(value = "SysMail对象", description = "邮件系统")
public class SysMail implements Serializable {

    @TableId(value = "mail_id", type = IdType.AUTO)
    private Long mailId;

    @ApiModelProperty("发信用户")
    private String from_user_name;
    @ApiModelProperty("收信角色")
    private String to_role_name;

    @ApiModelProperty("主题")
    private String subject;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Long getMailId() {
        return mailId;
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getTo_role_name() {
        return to_role_name;
    }

    public void setTo_role_name(String to_role_name) {
        this.to_role_name = to_role_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SysMail{" +
                "mailId=" + mailId +
                ", from_user_name=" + from_user_name +
                ", to_role_name=" + to_role_name +
                ", subject=" + subject +
                ", content=" + content +
                ", createTime=" + createTime +
                "}";
    }
}
