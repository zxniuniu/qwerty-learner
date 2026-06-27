package com.typing.model;

import java.time.LocalDateTime;

import lombok.Data;

/** 打字比赛 */
@Data
public class Competition {
  private String id;
  private String title;
  private String articleId;
  private String description;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String compStatus; // 0未开始 1进行中 2已结束
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private String status;

  // 关联展示（非持久化）
  private String articleTitle;
}
