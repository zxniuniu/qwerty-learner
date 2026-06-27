package com.typing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.typing.model.SpeedRecord;

@Mapper
public interface SpeedRecordMapper {

  @Insert("INSERT INTO ts_speed_record(id,user_code,user_name,mode,ref_id,ref_name,cpm,wpm,accuracy,duration,char_count,wrong_count,create_date,status) "
      + "VALUES(#{id},#{userCode},#{userName},#{mode},#{refId},#{refName},#{cpm},#{wpm},#{accuracy},#{duration},#{charCount},#{wrongCount},#{createDate},#{status})")
  int insert(SpeedRecord r);

  /** 速度排名：按用户取最好成绩聚合 */
  @Select("<script>SELECT user_code AS userCode, MAX(user_name) AS userName, MAX(cpm) AS cpm, MAX(wpm) AS wpm, "
      + "ROUND(AVG(accuracy)) AS accuracy, COUNT(1) AS recordCount FROM ts_speed_record WHERE status='0'"
      + "<if test=\"mode != null and mode != ''\"> AND mode = #{mode}</if>"
      + " GROUP BY user_code ORDER BY MAX(cpm) DESC</script>")
  List<SpeedRecord> findRanking(@Param("mode") String mode);

  /** 管理端：记录列表 */
  @Select("<script>SELECT * FROM ts_speed_record WHERE status='0'"
      + "<if test=\"mode != null and mode != ''\"> AND mode = #{mode}</if>"
      + " ORDER BY create_date DESC</script>")
  List<SpeedRecord> findRecords(@Param("mode") String mode);
}
