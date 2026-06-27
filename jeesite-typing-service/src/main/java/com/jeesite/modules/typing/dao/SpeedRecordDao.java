package com.jeesite.modules.typing.dao;

import java.util.List;

import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.common.mybatis.mapper.CrudDao;
import com.jeesite.modules.typing.entity.SpeedRecord;

/**
 * 打字速度记录 DAO
 */
@MyBatisDao
public interface SpeedRecordDao extends CrudDao<SpeedRecord> {

  /**
   * 速度排名：按用户聚合，取每个用户的最好成绩（最高 cpm）与平均正确率。
   * 支持按 mode、create_date 区间过滤，结果按最好成绩降序。
   */
  List<SpeedRecord> findRanking(SpeedRecord speedRecord);
}
