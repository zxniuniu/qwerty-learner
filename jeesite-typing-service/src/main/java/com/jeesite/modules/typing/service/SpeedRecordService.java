package com.jeesite.modules.typing.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.typing.dao.SpeedRecordDao;
import com.jeesite.modules.typing.entity.SpeedRecord;

/**
 * 打字速度记录 Service
 */
@Service
public class SpeedRecordService extends CrudService<SpeedRecordDao, SpeedRecord> {

  @Override
  public SpeedRecord get(String id) {
    return super.get(id);
  }

  @Override
  public Page<SpeedRecord> findPage(SpeedRecord speedRecord) {
    return super.findPage(speedRecord);
  }

  /**
   * 保存一条速度记录（玩家完赛后上报）。
   */
  @Transactional
  public void saveRecord(SpeedRecord record) {
    if (StringUtils.isBlank(record.getUserName())) {
      record.setUserName("匿名用户");
    }
    super.save(record);
  }

  /**
   * 速度排名：按用户取最好成绩，返回前 topN 名。
   */
  public List<SpeedRecord> findRanking(SpeedRecord speedRecord, int topN) {
    List<SpeedRecord> list = dao.findRanking(speedRecord);
    if (topN > 0 && list.size() > topN) {
      return list.subList(0, topN);
    }
    return list;
  }

  @Override
  @Transactional
  public void delete(SpeedRecord speedRecord) {
    super.delete(speedRecord);
  }
}
