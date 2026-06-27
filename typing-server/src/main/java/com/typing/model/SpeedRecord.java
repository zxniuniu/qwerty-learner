package com.typing.model;

import java.time.LocalDateTime;

import lombok.Data;

/** 打字速度记录 */
@Data
public class SpeedRecord {
  private String id;
  private String userCode;
  private String userName;
  private String mode; // word/article/pinyin/wubi/en
  private String refId;
  private String refName;
  private Integer cpm;
  private Integer wpm;
  private Integer accuracy;
  private Integer duration;
  private Integer charCount;
  private Integer wrongCount;
  private LocalDateTime createDate;
  private String status;

  // 排名聚合：该用户的记录条数（非持久化）
  private Integer recordCount;
}
