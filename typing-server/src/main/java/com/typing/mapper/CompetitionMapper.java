package com.typing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.typing.model.Competition;

@Mapper
public interface CompetitionMapper {

  @Select("<script>SELECT * FROM ts_competition WHERE status='0'"
      + "<if test=\"compStatus != null and compStatus != ''\"> AND comp_status = #{compStatus}</if>"
      + " ORDER BY start_time DESC, update_date DESC</script>")
  List<Competition> findList(@Param("compStatus") String compStatus);

  @Select("SELECT * FROM ts_competition WHERE id = #{id}")
  Competition findById(@Param("id") String id);

  @Insert("INSERT INTO ts_competition(id,title,article_id,description,start_time,end_time,comp_status,create_date,update_date,status) "
      + "VALUES(#{id},#{title},#{articleId},#{description},#{startTime},#{endTime},#{compStatus},#{createDate},#{updateDate},#{status})")
  int insert(Competition c);

  @Update("UPDATE ts_competition SET title=#{title},article_id=#{articleId},description=#{description},"
      + "start_time=#{startTime},end_time=#{endTime},comp_status=#{compStatus},update_date=#{updateDate} WHERE id=#{id}")
  int update(Competition c);

  @Delete("DELETE FROM ts_competition WHERE id = #{id}")
  int deleteById(@Param("id") String id);
}
