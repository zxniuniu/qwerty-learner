package com.jeesite.modules.typing.dao;

import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.common.mybatis.mapper.CrudDao;
import com.jeesite.modules.typing.entity.Article;

/**
 * 打字文章 DAO。标准 CRUD 由 JeeSite 基于 @Table 注解自动生成，无需额外 XML。
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {

}
