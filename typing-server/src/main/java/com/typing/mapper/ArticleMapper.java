package com.typing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.typing.model.Article;

@Mapper
public interface ArticleMapper {

  @Select("<script>SELECT * FROM ts_article WHERE status='0'"
      + "<if test=\"language != null and language != ''\"> AND language = #{language}</if>"
      + " ORDER BY sort ASC, update_date DESC</script>")
  List<Article> findList(@Param("language") String language);

  @Select("SELECT * FROM ts_article WHERE id = #{id}")
  Article findById(@Param("id") String id);

  @Insert("INSERT INTO ts_article(id,title,language,category,content,word_count,difficulty,sort,create_date,update_date,remarks,status) "
      + "VALUES(#{id},#{title},#{language},#{category},#{content},#{wordCount},#{difficulty},#{sort},#{createDate},#{updateDate},#{remarks},#{status})")
  int insert(Article a);

  @Update("UPDATE ts_article SET title=#{title},language=#{language},category=#{category},content=#{content},"
      + "word_count=#{wordCount},difficulty=#{difficulty},sort=#{sort},update_date=#{updateDate},remarks=#{remarks} WHERE id=#{id}")
  int update(Article a);

  @Delete("DELETE FROM ts_article WHERE id = #{id}")
  int deleteById(@Param("id") String id);
}
