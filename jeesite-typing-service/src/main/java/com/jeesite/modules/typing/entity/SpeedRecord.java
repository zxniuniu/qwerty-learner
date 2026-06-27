package com.jeesite.modules.typing.entity;

import java.util.Date;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 打字速度记录 Entity
 */
@Table(name = "ts_speed_record", alias = "a", label = "打字速度记录", columns = {
    @Column(name = "id", attrName = "id", label = "主键", isPK = true),
    @Column(name = "user_code", attrName = "userCode", label = "用户编码"),
    @Column(name = "user_name", attrName = "userName", label = "用户昵称", queryType = QueryType.LIKE),
    @Column(name = "mode", attrName = "mode", label = "练习模式"),
    @Column(name = "ref_id", attrName = "refId", label = "关联ID"),
    @Column(name = "ref_name", attrName = "refName", label = "关联名称", queryType = QueryType.LIKE),
    @Column(name = "cpm", attrName = "cpm", label = "字符每分", isQuery = false),
    @Column(name = "wpm", attrName = "wpm", label = "单词每分", isQuery = false),
    @Column(name = "accuracy", attrName = "accuracy", label = "正确率", isQuery = false),
    @Column(name = "duration", attrName = "duration", label = "用时秒", isQuery = false),
    @Column(name = "char_count", attrName = "charCount", label = "字符数", isQuery = false),
    @Column(name = "wrong_count", attrName = "wrongCount", label = "错误数", isQuery = false),
    @Column(includeEntity = DataEntity.class),
}, orderBy = "a.cpm DESC, a.create_date DESC")
public class SpeedRecord extends DataEntity<SpeedRecord> {

  private static final long serialVersionUID = 1L;

  private String userCode; // 用户编码
  private String userName; // 用户昵称
  private String mode; // 练习模式：word/article/pinyin/wubi/en
  private String refId; // 关联ID（文章或词典）
  private String refName; // 关联名称
  private Integer cpm; // 字符每分
  private Integer wpm; // 单词每分
  private Integer accuracy; // 正确率 0-100
  private Integer duration; // 用时（秒）
  private Integer charCount; // 字符数
  private Integer wrongCount; // 错误数

  // 排名查询用：起止日期（非持久化字段）
  private Date beginDate;
  private Date endDate;
  // 排名聚合：该用户的记录条数（非持久化字段）
  private Integer recordCount;

  public SpeedRecord() {
    this(null);
  }

  public SpeedRecord(String id) {
    super(id);
  }

  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getRefId() {
    return refId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public String getRefName() {
    return refName;
  }

  public void setRefName(String refName) {
    this.refName = refName;
  }

  public Integer getCpm() {
    return cpm;
  }

  public void setCpm(Integer cpm) {
    this.cpm = cpm;
  }

  public Integer getWpm() {
    return wpm;
  }

  public void setWpm(Integer wpm) {
    this.wpm = wpm;
  }

  public Integer getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(Integer accuracy) {
    this.accuracy = accuracy;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Integer getCharCount() {
    return charCount;
  }

  public void setCharCount(Integer charCount) {
    this.charCount = charCount;
  }

  public Integer getWrongCount() {
    return wrongCount;
  }

  public void setWrongCount(Integer wrongCount) {
    this.wrongCount = wrongCount;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getRecordCount() {
    return recordCount;
  }

  public void setRecordCount(Integer recordCount) {
    this.recordCount = recordCount;
  }
}
