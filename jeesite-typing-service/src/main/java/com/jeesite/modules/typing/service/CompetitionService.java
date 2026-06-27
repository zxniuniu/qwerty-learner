package com.jeesite.modules.typing.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.typing.dao.CompetitionDao;
import com.jeesite.modules.typing.entity.Competition;

/**
 * 打字比赛 Service
 */
@Service
public class CompetitionService extends CrudService<CompetitionDao, Competition> {

  @Override
  public Competition get(String id) {
    return super.get(id);
  }

  @Override
  public Page<Competition> findPage(Competition competition) {
    return super.findPage(competition);
  }

  @Override
  public List<Competition> findList(Competition competition) {
    return super.findList(competition);
  }

  @Override
  @Transactional
  public void save(Competition competition) {
    super.save(competition);
  }

  @Override
  @Transactional
  public void updateStatus(Competition competition) {
    super.updateStatus(competition);
  }

  @Override
  @Transactional
  public void delete(Competition competition) {
    super.delete(competition);
  }
}
