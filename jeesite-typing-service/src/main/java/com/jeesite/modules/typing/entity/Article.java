package com.jeesite.modules.typing.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 打字文章 Entity
 */
@Table(name = "ts_article", alias = "a", label = "打字文章", columns = {
    @Column(name = "id", attrName = "id", label = "主键", isPK = true),
    @Column(name = "title", attrName = "title", label = "标题", queryType = QueryType.LIKE),
    @Column(name = "language", attrName = "language", label = "语言"),
    @Column(name = "category", attrName = "category", label = "分类", queryType = QueryType.LIKE),
    @Column(name = "content", attrName = "content", label = "正文", isQuery = false),
    @Column(name = "word_count", attrName = "wordCount", label = "字数", isQuery = false),
    @Column(name = "difficulty", attrName = "difficulty", label = "难度"),
    @Column(name = "sort", attrName = "sort", label = "排序号", isQuery = false),
    @Column(includeEntity = DataEntity.class),
}, orderBy = "a.sort ASC, a.update_date DESC")
public class Article extends DataEntity<Article> {

  private static final long serialVersionUID = 1L;

  private String title; // 标题
  private String language; // 语言：zh / en
  private String category; // 分类
  private String content; // 正文
  private Integer wordCount; // 字数
  private String difficulty; // 难度：1易 2中 3难
  private Integer sort; // 排序号

  public Article() {
    this(null);
  }

  public Article(String id) {
    super(id);
  }

  @NotBlank(message = "标题不能为空")
  @Size(min = 0, max = 200, message = "标题长度不能超过 200 个字符")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @NotBlank(message = "正文不能为空")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getWordCount() {
    return wordCount;
  }

  public void setWordCount(Integer wordCount) {
    this.wordCount = wordCount;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public Integer getSort() {
    return sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }
}
