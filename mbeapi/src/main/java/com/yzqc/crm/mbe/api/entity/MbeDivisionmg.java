package com.yzqc.crm.mbe.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;




@Entity
@Table(name = "mbe_divisionmg")
@NamedQuery(name = "MbeDivisionmg.findAll", query = "SELECT m FROM MbeDivisionmg m")
public class MbeDivisionmg implements Serializable {

	private static final long serialVersionUID = 5940840667653941909L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_divisionmg")
	private Long pkDivisionmg;
	
	@NotNull(message="编码不能为空")
	@Pattern(regexp = "\\d{11}", message = "编码必须为11位数字")
	private String code;

	private String creater;
	
	@Length(min=1,max=20, message="名称的长度必须在1到20之间")
	private String name;

	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createtime;

	private int curcore;
	
	@Length(min=1,max=20, message="描述的长度为1到128个字之间")
	private String description;

	private int dr;
	
	@Column(name="pk_group")
	private String pkGroup;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date ts;

	private int vstatus;

	public MbeDivisionmg() {
	}

	public Long getPkDivisionmg() {
		return this.pkDivisionmg;
	}

	public void setPkDivisionmg(Long pkDivisionmg) {
		this.pkDivisionmg = pkDivisionmg;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreater() {
		return this.creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getCurcore() {
		return this.curcore;
	}

	public void setCurcore(int curcore) {
		this.curcore = curcore;
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

	public String getPkGroup() {
		return this.pkGroup;
	}

	public void setPkGroup(String pkGroup) {
		this.pkGroup = pkGroup;
	}

	public Date getTs() {
		return this.ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public int getVstatus() {
		return this.vstatus;
	}

	public void setVstatus(int vstatus) {
		this.vstatus = vstatus;
	}
	
}
