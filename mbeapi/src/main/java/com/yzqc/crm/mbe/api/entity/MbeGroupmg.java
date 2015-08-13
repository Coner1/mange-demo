package com.yzqc.crm.mbe.api.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the mbe_groupmg database table.
 */
@Entity
@Table(name = "mbe_groupmg")
@NamedQuery(name = "MbeGroupmg.findAll", query = "SELECT m FROM MbeGroupmg m")
public class MbeGroupmg implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_mbe_groupmg")
    private Long pkMbeGroupmg;

    @NotNull(message = "编码不能为空")
    @Pattern(regexp = "\\d{11}", message = "编码必须为11位数字")
    private String code;

    private BigInteger creater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createtime;

    @Length(min = 0, max = 128, message = "描述的长度必须在0到128之间")
    private String description;

    private int dr;

    @NotNull(message = "名称不能为空")
    @Length(min = 1, max = 20, message = "名称的长度必须在1到20之间")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date ts;

    private int vstatus;

    public MbeGroupmg() {
    }

    public String getCode() {
        return code;
    }

    public BigInteger getCreater() {
        return this.creater;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreater(BigInteger creater) {
        this.creater = creater;
    }


    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDr() {
        return this.dr;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVstatus() {
        return this.vstatus;
    }

    public void setVstatus(int vstatus) {
        this.vstatus = vstatus;
    }

    public Long getPkMbeGroupmg() {
        return pkMbeGroupmg;
    }

    public void setPkMbeGroupmg(Long pkMbeGroupmg) {
        this.pkMbeGroupmg = pkMbeGroupmg;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "MbeGroupmg{" +
                "pkMbeGroupmg=" + pkMbeGroupmg +
                ", code=" + code +
                ", creater=" + creater +
                ", createtime=" + createtime +
                ", description='" + description + '\'' +
                ", dr=" + dr +
                ", name='" + name + '\'' +
                ", ts=" + ts +
                ", vstatus=" + vstatus +
                '}';
    }
}