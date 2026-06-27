package com.jeesite.modules.typing.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.typing.dao.ArticleDao;
import com.jeesite.modules.typing.entity.Article;

/**
 * 打字文章 Service
 */
@Service
public class ArticleService extends CrudService<ArticleDao, Article> {

  @Override
  public Article get(String id) {
    return super.get(id);
  }

  @Override
  public Page<Article> findPage(Article article) {
    return super.findPage(article);
  }

  @Override
  @Transactional
  public void save(Article article) {
    // 保存时根据正文自动计算字数（去除空白字符）
    if (article.getContent() != null) {
      article.setWordCount(article.getContent().replaceAll("\\s", "").length());
    }
    super.save(article);
  }

  @Override
  @Transactional
  public void updateStatus(Article article) {
    super.updateStatus(article);
  }

  @Override
  @Transactional
  public void delete(Article article) {
    super.delete(article);
  }
}
