package com.typing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.typing.model.CompetitionResult;

@Mapper
public interface CompetitionResultMapper {

  @Insert("INSERT INTO ts_competition_result(id,competition_id,user_code,user_name,cpm,accuracy,duration,char_count,submit_time,status) "
      + "VALUES(#{id},#{competitionId},#{userCode},#{userName},#{cpm},#{accuracy},#{duration},#{charCount},#{submitTime},#{status})")
  int insert(CompetitionResult r);

  /** 某场比赛全部成绩，按 cpm 降序、用时升序（去重与名次在 Service 处理） */
  @Select("SELECT * FROM ts_competition_result WHERE status='0' AND competition_id = #{competitionId} "
      + "ORDER BY cpm DESC, duration ASC")
  List<CompetitionResult> findByCompetition(@Param("competitionId") String competitionId);
}
