package com.typing.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.typing.mapper.CompetitionResultMapper;
import com.typing.model.CompetitionResult;

@Service
public class CompetitionResultService {

  private final CompetitionResultMapper mapper;

  public CompetitionResultService(CompetitionResultMapper mapper) {
    this.mapper = mapper;
  }

  public CompetitionResult submit(CompetitionResult r) {
    r.setId(UUID.randomUUID().toString().replace("-", ""));
    if (r.getSubmitTime() == null) r.setSubmitTime(LocalDateTime.now());
    if (r.getStatus() == null) r.setStatus("0");
    if (!StringUtils.hasText(r.getUserName())) r.setUserName("匿名用户");
    mapper.insert(r);
    return r;
  }

  /** 排行榜：按用户去重保留最好成绩，赋名次，取前 top 名 */
  public List<CompetitionResult> leaderboard(String competitionId, int top) {
    List<CompetitionResult> all = mapper.findByCompetition(competitionId);
    List<CompetitionResult> board = new ArrayList<>();
    Set<String> seen = new HashSet<>();
    int rank = 0;
    for (CompetitionResult r : all) {
      String key = StringUtils.hasText(r.getUserCode()) ? r.getUserCode() : r.getId();
      if (seen.contains(key)) continue;
      seen.add(key);
      rank++;
      r.setRankNo(rank);
      board.add(r);
      if (top > 0 && board.size() >= top) break;
    }
    return board;
  }
}
