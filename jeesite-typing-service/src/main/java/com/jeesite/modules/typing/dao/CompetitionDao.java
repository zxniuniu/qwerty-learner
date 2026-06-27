package com.jeesite.modules.typing.dao;

import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.common.mybatis.mapper.CrudDao;
import com.jeesite.modules.typing.entity.Competition;

/**
 * 打字比赛 DAO
 */
@MyBatisDao
public interface CompetitionDao extends CrudDao<Competition> {

}
