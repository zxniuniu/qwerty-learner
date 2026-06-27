package com.typing.model;

import java.time.LocalDateTime;

import lombok.Data;

/** 打字文章 */
@Data
public class Article {
  private String id;
  private String title;
  private String language; // zh / en
  private String category;
  private String content;
  private Integer wordCount;
  private String difficulty; // 1易 2中 3难
  private Integer sort;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private String remarks;
  private String status; // 0正常 1删除
}
