package com.jeesite.modules.typing.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 打字比赛成绩 Entity
 */
@Table(name = "ts_competition_result", alias = "a", label = "打字比赛成绩", columns = {
    @Column(name = "id", attrName = "id", label = "主键", isPK = true),
    @Column(name = "competition_id", attrName = "competitionId", label = "比赛ID"),
    @Column(name = "user_code", attrName = "userCode", label = "用户编码"),
    @Column(name = "user_name", attrName = "userName", label = "用户昵称", queryType = QueryType.LIKE),
    @Column(name = "cpm", attrName = "cpm", label = "字符每分", isQuery = false),
    @Column(name = "accuracy", attrName = "accuracy", label = "正确率", isQuery = false),
    @Column(name = "duration", attrName = "duration", label = "用时秒", isQuery = false),
    @Column(name = "char_count", attrName = "charCount", label = "字符数", isQuery = false),
    @Column(name = "submit_time", attrName = "submitTime", label = "提交时间", isQuery = false),
    @Column(includeEntity = DataEntity.class),
}, orderBy = "a.cpm DESC, a.duration ASC")
public class CompetitionResult extends DataEntity<CompetitionResult> {

  private static final long serialVersionUID = 1L;

  private String competitionId; // 比赛ID
  private String userCode; // 用户编码
  private String userName; // 用户昵称
  private Integer cpm; // 字符每分
  private Integer accuracy; // 正确率 0-100
  private Integer duration; // 用时（秒）
  private Integer charCount; // 字符数
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date submitTime; // 提交时间

  // 排行展示用（非持久化）：名次
  private Integer rankNo;

  public CompetitionResult() {
    this(null);
  }

  public CompetitionResult(String id) {
    super(id);
  }

  public String getCompetitionId() {
    return competitionId;
  }

  public void setCompetitionId(String competitionId) {
    this.competitionId = competitionId;
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

  public Integer getCpm() {
    return cpm;
  }

  public void setCpm(Integer cpm) {
    this.cpm = cpm;
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

  public Date getSubmitTime() {
    return submitTime;
  }

  public void setSubmitTime(Date submitTime) {
    this.submitTime = submitTime;
  }

  public Integer getRankNo() {
    return rankNo;
  }

  public void setRankNo(Integer rankNo) {
    this.rankNo = rankNo;
  }
}
