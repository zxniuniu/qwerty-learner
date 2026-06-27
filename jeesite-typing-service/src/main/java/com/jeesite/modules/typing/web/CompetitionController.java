package com.jeesite.modules.typing.web;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.typing.entity.Competition;
import com.jeesite.modules.typing.entity.CompetitionResult;
import com.jeesite.modules.typing.service.CompetitionResultService;
import com.jeesite.modules.typing.service.CompetitionService;

/**
 * 打字比赛 管理端 Controller（供 jeesite-vue 后台调用）
 */
@Controller
@RequestMapping(value = "${adminPath}/typing/competition")
public class CompetitionController extends BaseController {

  @Autowired
  private CompetitionService competitionService;
  @Autowired
  private CompetitionResultService competitionResultService;

  @ModelAttribute
  public Competition get(String id, boolean isNewRecord) {
    return competitionService.get(id, isNewRecord);
  }

  @RequiresPermissions("typing:competition:view")
  @RequestMapping(value = { "list", "" })
  public String list(Competition competition, Model model) {
    model.addAttribute("competition", competition);
    return "modules/typing/competitionList";
  }

  @RequiresPermissions("typing:competition:view")
  @RequestMapping(value = "listData")
  @ResponseBody
  public Page<Competition> listData(Competition competition, HttpServletRequest request, HttpServletResponse response) {
    competition.setPage(new Page<>(request, response));
    return competitionService.findPage(competition);
  }

  @RequiresPermissions("typing:competition:view")
  @RequestMapping(value = "form")
  public String form(Competition competition, Model model) {
    model.addAttribute("competition", competition);
    return "modules/typing/competitionForm";
  }

  @RequiresPermissions("typing:competition:edit")
  @PostMapping(value = "save")
  @ResponseBody
  public String save(@Validated Competition competition) {
    competitionService.save(competition);
    return renderResult(Global.TRUE, text("保存打字比赛成功！"));
  }

  @RequiresPermissions("typing:competition:edit")
  @RequestMapping(value = "delete")
  @ResponseBody
  public String delete(Competition competition) {
    competitionService.delete(competition);
    return renderResult(Global.TRUE, text("删除打字比赛成功！"));
  }

  /**
   * 管理端查看某场比赛的成绩排行榜。
   */
  @RequiresPermissions("typing:competition:view")
  @RequestMapping(value = "leaderboard")
  @ResponseBody
  public List<CompetitionResult> leaderboard(@RequestParam String competitionId,
      @RequestParam(defaultValue = "50") int top) {
    return competitionResultService.findLeaderboard(competitionId, top);
  }
}
