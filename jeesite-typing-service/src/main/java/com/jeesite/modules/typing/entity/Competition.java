package com.jeesite.modules.typing.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 打字比赛 Entity
 */
@Table(name = "ts_competition", alias = "a", label = "打字比赛", columns = {
    @Column(name = "id", attrName = "id", label = "主键", isPK = true),
    @Column(name = "title", attrName = "title", label = "比赛标题", queryType = QueryType.LIKE),
    @Column(name = "article_id", attrName = "articleId", label = "指定文章ID"),
    @Column(name = "description", attrName = "description", label = "比赛说明", isQuery = false),
    @Column(name = "start_time", attrName = "startTime", label = "开始时间", isQuery = false),
    @Column(name = "end_time", attrName = "endTime", label = "结束时间", isQuery = false),
    @Column(name = "comp_status", attrName = "compStatus", label = "比赛状态"),
    @Column(includeEntity = DataEntity.class),
}, orderBy = "a.start_time DESC, a.update_date DESC")
public class Competition extends DataEntity<Competition> {

  private static final long serialVersionUID = 1L;

  public static final String COMP_NOT_START = "0"; // 未开始
  public static final String COMP_RUNNING = "1"; // 进行中
  public static final String COMP_FINISHED = "2"; // 已结束

  private String title; // 比赛标题
  private String articleId; // 指定文章ID
  private String description; // 比赛说明
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startTime; // 开始时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endTime; // 结束时间
  private String compStatus; // 比赛状态：0未开始 1进行中 2已结束

  // 关联展示用（非持久化）
  private String articleTitle;

  public Competition() {
    this(null);
  }

  public Competition(String id) {
    super(id);
  }

  @NotBlank(message = "比赛标题不能为空")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArticleId() {
    return articleId;
  }

  public void setArticleId(String articleId) {
    this.articleId = articleId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getCompStatus() {
    return compStatus;
  }

  public void setCompStatus(String compStatus) {
    this.compStatus = compStatus;
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }
}
