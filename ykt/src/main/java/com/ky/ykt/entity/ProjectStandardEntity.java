package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

import java.math.BigDecimal;

/**
 * @ClassName ProjectStandardEntity
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/17
 **/
public class ProjectStandardEntity extends BaseEntity {
    private String name;
    private String note;
    private String content;
    private BigDecimal amount;
    private BigDecimal num;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
