package com.typing.model;

import java.time.LocalDateTime;

import lombok.Data;

/** 打字比赛成绩 */
@Data
public class CompetitionResult {
  private String id;
  private String competitionId;
  private String userCode;
  private String userName;
  private Integer cpm;
  private Integer accuracy;
  private Integer duration;
  private Integer charCount;
  private LocalDateTime submitTime;
  private String status;

  // 排行展示（非持久化）
  private Integer rankNo;
}
