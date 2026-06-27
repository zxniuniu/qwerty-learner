package com.typing.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.typing.mapper.ArticleMapper;
import com.typing.model.Article;

@Service
public class ArticleService {

  private final ArticleMapper mapper;

  public ArticleService(ArticleMapper mapper) {
    this.mapper = mapper;
  }

  public List<Article> findList(String language) {
    return mapper.findList(language);
  }

  public Article get(String id) {
    return mapper.findById(id);
  }

  public Article save(Article a) {
    if (a.getContent() != null) {
      a.setWordCount(a.getContent().replaceAll("\\s", "").length());
    }
    LocalDateTime now = LocalDateTime.now();
    a.setUpdateDate(now);
    if (a.getId() == null || a.getId().isEmpty()) {
      a.setId(UUID.randomUUID().toString().replace("-", ""));
      a.setCreateDate(now);
      if (a.getStatus() == null) a.setStatus("0");
      if (a.getLanguage() == null) a.setLanguage("zh");
      if (a.getSort() == null) a.setSort(30);
      mapper.insert(a);
    } else {
      mapper.update(a);
    }
    return a;
  }

  public void delete(String id) {
    mapper.deleteById(id);
  }
}
