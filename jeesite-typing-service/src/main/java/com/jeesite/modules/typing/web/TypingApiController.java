package com.jeesite.modules.typing.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.typing.entity.Article;
import com.jeesite.modules.typing.entity.Competition;
import com.jeesite.modules.typing.entity.CompetitionResult;
import com.jeesite.modules.typing.entity.SpeedRecord;
import com.jeesite.modules.typing.service.ArticleService;
import com.jeesite.modules.typing.service.CompetitionResultService;
import com.jeesite.modules.typing.service.CompetitionService;
import com.jeesite.modules.typing.service.SpeedRecordService;

/**
 * 打字练习 玩家端开放 API（供 React 前端调用）。
 *
 * 安全说明：本控制器路径 /api/typing/** 需在你的安全过滤链中放行（允许匿名或按 token 鉴权），
 * 详见 README「安全与跨域」。@CrossOrigin 仅为方便本地联调，生产请按需收紧来源。
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/typing")
public class TypingApiController {

  @Autowired
  private ArticleService articleService;
  @Autowired
  private SpeedRecordService speedRecordService;
  @Autowired
  private CompetitionService competitionService;
  @Autowired
  private CompetitionResultService competitionResultService;

  // ============ 文章 ============

  /** 文章列表（可按语言过滤），不返回正文以减小体积 */
  @GetMapping("article/list")
  public List<Article> articleList(@RequestParam(required = false) String language) {
    Article query = new Article();
    query.setStatus(DataEntity.STATUS_NORMAL);
    if (StringUtils.isNotBlank(language)) {
      query.setLanguage(language);
    }
    List<Article> list = articleService.findList(query);
    // 列表不带正文
    for (Article a : list) {
      a.setContent(null);
    }
    return list;
  }

  /** 文章详情（含正文） */
  @GetMapping("article/get")
  public Article articleGet(@RequestParam String id) {
    return articleService.get(id);
  }

  // ============ 速度记录与排名 ============

  /** 上报一条速度记录 */
  @PostMapping("speed/save")
  public Map<String, Object> speedSave(@RequestBody SpeedRecord record) {
    fillCurrentUser(record);
    speedRecordService.saveRecord(record);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    ret.put("id", record.getId());
    return ret;
  }

  /** 速度排名（按用户取最好成绩），支持 mode 过滤与 top 限制 */
  @GetMapping("speed/ranking")
  public List<SpeedRecord> speedRanking(@RequestParam(required = false) String mode,
      @RequestParam(defaultValue = "50") int top) {
    SpeedRecord query = new SpeedRecord();
    if (StringUtils.isNotBlank(mode)) {
      query.setMode(mode);
    }
    return speedRecordService.findRanking(query, top);
  }

  // ============ 比赛 ============

  /** 比赛列表（可按状态过滤：0未开始 1进行中 2已结束） */
  @GetMapping("competition/list")
  public List<Competition> competitionList(@RequestParam(required = false) String compStatus) {
    Competition query = new Competition();
    query.setStatus(DataEntity.STATUS_NORMAL);
    if (StringUtils.isNotBlank(compStatus)) {
      query.setCompStatus(compStatus);
    }
    return competitionService.findList(query);
  }

  /** 比赛详情（含指定文章正文，便于前端直接开打） */
  @GetMapping("competition/get")
  public Map<String, Object> competitionGet(@RequestParam String id) {
    Competition competition = competitionService.get(id);
    Map<String, Object> ret = new HashMap<>();
    ret.put("competition", competition);
    if (competition != null && StringUtils.isNotBlank(competition.getArticleId())) {
      ret.put("article", articleService.get(competition.getArticleId()));
    }
    return ret;
  }

  /** 提交比赛成绩 */
  @PostMapping("competition/submit")
  public Map<String, Object> competitionSubmit(@RequestBody CompetitionResult result) {
    fillCurrentUser(result);
    competitionResultService.submit(result);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    ret.put("id", result.getId());
    return ret;
  }

  /** 比赛排行榜 */
  @GetMapping("competition/leaderboard")
  public List<CompetitionResult> competitionLeaderboard(@RequestParam String competitionId,
      @RequestParam(defaultValue = "50") int top) {
    return competitionResultService.findLeaderboard(competitionId, top);
  }

  // ============ 工具 ============

  /** 速度记录：补全当前登录用户信息（未传且已登录时） */
  private void fillCurrentUser(SpeedRecord record) {
    if (StringUtils.isBlank(record.getUserCode())) {
      String[] u = currentUser();
      record.setUserCode(u[0]);
      if (StringUtils.isBlank(record.getUserName())) {
        record.setUserName(u[1]);
      }
    }
  }

  /** 比赛成绩：补全当前登录用户信息 */
  private void fillCurrentUser(CompetitionResult result) {
    if (StringUtils.isBlank(result.getUserCode())) {
      String[] u = currentUser();
      result.setUserCode(u[0]);
      if (StringUtils.isBlank(result.getUserName())) {
        result.setUserName(u[1]);
      }
    }
  }

  /** 返回 [userCode, userName]，未登录时为 ["", ""] */
  private String[] currentUser() {
    try {
      Object user = UserUtils.getUser();
      if (user != null) {
        // 通过反射兼容不同版本，避免强依赖 User 的具体方法签名
        String code = invokeString(user, "getUserCode");
        String name = invokeString(user, "getUserName");
        if (StringUtils.isNotBlank(code)) {
          return new String[] { code, StringUtils.defaultString(name, "") };
        }
      }
    } catch (Exception e) {
      // 未登录或开放访问，忽略
    }
    return new String[] { "", "" };
  }

  private String invokeString(Object target, String method) {
    try {
      Object v = target.getClass().getMethod(method).invoke(target);
      return v != null ? v.toString() : null;
    } catch (Exception e) {
      return null;
    }
  }
}
