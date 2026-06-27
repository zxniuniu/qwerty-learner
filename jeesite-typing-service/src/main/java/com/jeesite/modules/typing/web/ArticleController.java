package com.jeesite.modules.typing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.typing.entity.Article;
import com.jeesite.modules.typing.service.ArticleService;

/**
 * 打字文章 管理端 Controller（供 jeesite-vue 后台调用）
 */
@Controller
@RequestMapping(value = "${adminPath}/typing/article")
public class ArticleController extends BaseController {

  @Autowired
  private ArticleService articleService;

  @ModelAttribute
  public Article get(String id, boolean isNewRecord) {
    return articleService.get(id, isNewRecord);
  }

  @RequiresPermissions("typing:article:view")
  @RequestMapping(value = { "list", "" })
  public String list(Article article, Model model) {
    model.addAttribute("article", article);
    return "modules/typing/articleList";
  }

  @RequiresPermissions("typing:article:view")
  @RequestMapping(value = "listData")
  @ResponseBody
  public Page<Article> listData(Article article, HttpServletRequest request, HttpServletResponse response) {
    article.setPage(new Page<>(request, response));
    return articleService.findPage(article);
  }

  @RequiresPermissions("typing:article:view")
  @RequestMapping(value = "form")
  public String form(Article article, Model model) {
    model.addAttribute("article", article);
    return "modules/typing/articleForm";
  }

  @RequiresPermissions("typing:article:edit")
  @PostMapping(value = "save")
  @ResponseBody
  public String save(@Validated Article article) {
    articleService.save(article);
    return renderResult(Global.TRUE, text("保存打字文章成功！"));
  }

  @RequiresPermissions("typing:article:edit")
  @RequestMapping(value = "delete")
  @ResponseBody
  public String delete(Article article) {
    articleService.delete(article);
    return renderResult(Global.TRUE, text("删除打字文章成功！"));
  }
}
