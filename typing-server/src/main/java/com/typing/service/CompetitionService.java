package com.typing.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.typing.mapper.ArticleMapper;
import com.typing.mapper.CompetitionMapper;
import com.typing.model.Article;
import com.typing.model.Competition;

@Service
public class CompetitionService {

  private final CompetitionMapper mapper;
  private final ArticleMapper articleMapper;

  public CompetitionService(CompetitionMapper mapper, ArticleMapper articleMapper) {
    this.mapper = mapper;
    this.articleMapper = articleMapper;
  }

  public List<Competition> findList(String compStatus) {
    return mapper.findList(compStatus);
  }

  public Competition get(String id) {
    Competition c = mapper.findById(id);
    if (c != null && StringUtils.hasText(c.getArticleId())) {
      Article a = articleMapper.findById(c.getArticleId());
      if (a != null) c.setArticleTitle(a.getTitle());
    }
    return c;
  }

  public Competition save(Competition c) {
    LocalDateTime now = LocalDateTime.now();
    c.setUpdateDate(now);
    if (c.getId() == null || c.getId().isEmpty()) {
      c.setId(UUID.randomUUID().toString().replace("-", ""));
      c.setCreateDate(now);
      if (c.getStatus() == null) c.setStatus("0");
      if (c.getCompStatus() == null) c.setCompStatus("0");
      mapper.insert(c);
    } else {
      mapper.update(c);
    }
    return c;
  }

  public void delete(String id) {
    mapper.deleteById(id);
  }
}
