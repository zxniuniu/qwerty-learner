package com.jeesite.modules.typing.dao;

import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.common.mybatis.mapper.CrudDao;
import com.jeesite.modules.typing.entity.CompetitionResult;

/**
 * 打字比赛成绩 DAO。排行榜直接用标准 findList（实体 orderBy 已按 cpm 降序、用时升序），
 * 名次与按用户去重在 Service 层处理。
 */
@MyBatisDao
public interface CompetitionResultDao extends CrudDao<CompetitionResult> {

}
