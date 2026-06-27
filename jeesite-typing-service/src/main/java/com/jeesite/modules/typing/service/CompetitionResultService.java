package com.jeesite.modules.typing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.typing.dao.CompetitionResultDao;
import com.jeesite.modules.typing.entity.CompetitionResult;

/**
 * 打字比赛成绩 Service
 */
@Service
public class CompetitionResultService extends CrudService<CompetitionResultDao, CompetitionResult> {

  @Override
  public CompetitionResult get(String id) {
    return super.get(id);
  }

  @Override
  public Page<CompetitionResult> findPage(CompetitionResult result) {
    return super.findPage(result);
  }

  @Override
  public List<CompetitionResult> findList(CompetitionResult result) {
    return super.findList(result);
  }

  /**
   * 提交比赛成绩。
   */
  @Transactional
  public void submit(CompetitionResult result) {
    if (StringUtils.isBlank(result.getUserName())) {
      result.setUserName("匿名用户");
    }
    if (result.getSubmitTime() == null) {
      result.setSubmitTime(new Date());
    }
    super.save(result);
  }

  /**
   * 比赛排行榜：按 cpm 降序、用时升序，按用户去重保留最好成绩，并赋予名次。
   *
   * @param competitionId 比赛ID
   * @param topN          返回前 N 名（<=0 表示全部）
   */
  public List<CompetitionResult> findLeaderboard(String competitionId, int topN) {
    CompetitionResult query = new CompetitionResult();
    query.setCompetitionId(competitionId);
    // 实体 orderBy 已是 cpm DESC, duration ASC
    List<CompetitionResult> all = super.findList(query);

    List<CompetitionResult> board = new ArrayList<>();
    Set<String> seenUsers = new HashSet<>();
    int rank = 0;
    for (CompetitionResult r : all) {
      String key = StringUtils.isNotBlank(r.getUserCode()) ? r.getUserCode() : r.getId();
      if (seenUsers.contains(key)) {
        continue; // 同一用户只保留最好成绩
      }
      seenUsers.add(key);
      rank++;
      r.setRankNo(rank);
      board.add(r);
      if (topN > 0 && board.size() >= topN) {
        break;
      }
    }
    return board;
  }
}
