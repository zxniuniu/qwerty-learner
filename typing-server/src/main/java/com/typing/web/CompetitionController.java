package com.typing.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.typing.model.Competition;
import com.typing.model.CompetitionResult;
import com.typing.service.CompetitionResultService;
import com.typing.service.CompetitionService;

/** 打字比赛：玩家端 + 管理端 */
@RestController
public class CompetitionController {

  private final CompetitionService competitionService;
  private final CompetitionResultService resultService;

  public CompetitionController(CompetitionService competitionService, CompetitionResultService resultService) {
    this.competitionService = competitionService;
    this.resultService = resultService;
  }

  /** 玩家端：比赛列表 */
  @GetMapping("/api/typing/competition/list")
  public List<Competition> list(@RequestParam(required = false) String compStatus) {
    return competitionService.findList(compStatus);
  }

  /** 玩家端：比赛详情 */
  @GetMapping("/api/typing/competition/get")
  public Competition get(@RequestParam String id) {
    return competitionService.get(id);
  }

  /** 玩家端：提交成绩 */
  @PostMapping("/api/typing/competition/submit")
  public Map<String, Object> submit(@RequestBody CompetitionResult result) {
    CompetitionResult saved = resultService.submit(result);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    ret.put("id", saved.getId());
    return ret;
  }

  /** 玩家端/管理端：排行榜 */
  @GetMapping("/api/typing/competition/leaderboard")
  public List<CompetitionResult> leaderboard(@RequestParam String competitionId,
      @RequestParam(defaultValue = "50") int top) {
    return resultService.leaderboard(competitionId, top);
  }

  /** 管理端：新增/编辑比赛 */
  @PostMapping("/api/admin/competition/save")
  public Map<String, Object> save(@RequestBody Competition competition) {
    Competition saved = competitionService.save(competition);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    ret.put("id", saved.getId());
    return ret;
  }

  /** 管理端：删除比赛 */
  @PostMapping("/api/admin/competition/delete")
  public Map<String, Object> delete(@RequestParam String id) {
    competitionService.delete(id);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    return ret;
  }
}
