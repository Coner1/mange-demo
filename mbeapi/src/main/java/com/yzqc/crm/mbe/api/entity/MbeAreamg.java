package com.yzqc.crm.mbe.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the mbe_areamg database table.
 * 
 */
@Entity
@Table(name = "mbe_areamg")
@NamedQuery(name = "MbeAreamg.findAll", query = "SELECT m FROM MbeAreamg m")
public class MbeAreamg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5940840667653941909L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pk_areamg")
	private Long pkAreamg;

	private String code;

	private String creater;

	@Temporal(TemporalType.DATE)
	private Date cteatetime;

	private String description;

	private int dr;

	private String name;

	private Timestamp ts;

	private int vstatus;

	public MbeAreamg() {
	}

	public Long getPkAreamg() {
		return pkAreamg;
	}

	public void setPkAreamg(Long pkAreamg) {
		this.pkAreamg = pkAreamg;
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

	public Date getCteatetime() {
		return this.cteatetime;
	}

	public void setCteatetime(Date cteatetime) {
		this.cteatetime = cteatetime;
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

	public Timestamp getTs() {
		return this.ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public int getVstatus() {
		return this.vstatus;
	}

	public void setVstatus(int vstatus) {
		this.vstatus = vstatus;
	}

}