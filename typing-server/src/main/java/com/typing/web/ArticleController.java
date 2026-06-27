package com.typing.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.typing.model.Article;
import com.typing.service.ArticleService;

/** 文章：玩家端读取 + 管理端 CRUD */
@RestController
public class ArticleController {

  private final ArticleService service;

  public ArticleController(ArticleService service) {
    this.service = service;
  }

  /** 玩家端：文章列表（不含正文） */
  @GetMapping("/api/typing/article/list")
  public List<Article> list(@RequestParam(required = false) String language) {
    List<Article> list = service.findList(language);
    list.forEach(a -> a.setContent(null));
    return list;
  }

  /** 玩家端：文章详情（含正文） */
  @GetMapping("/api/typing/article/get")
  public Article get(@RequestParam String id) {
    return service.get(id);
  }

  /** 管理端：列表（含正文，用于编辑） */
  @GetMapping("/api/admin/article/list")
  public List<Article> adminList(@RequestParam(required = false) String language) {
    return service.findList(language);
  }

  /** 管理端：新增/编辑 */
  @PostMapping("/api/admin/article/save")
  public Map<String, Object> save(@RequestBody Article article) {
    Article saved = service.save(article);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    ret.put("id", saved.getId());
    return ret;
  }

  /** 管理端：删除 */
  @PostMapping("/api/admin/article/delete")
  public Map<String, Object> delete(@RequestParam String id) {
    service.delete(id);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    return ret;
  }
}
