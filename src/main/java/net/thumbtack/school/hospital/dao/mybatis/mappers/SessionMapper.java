package net.thumbtack.school.hospital.dao.mybatis.mappers;

import org.apache.ibatis.annotations.*;
@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO session (user_id, uuid)"
            + " VALUES (#{id}, #{uuid})")
    void insert(@Param("id") int id, @Param("uuid") String uuid);

    @Delete("DELETE FROM session WHERE uuid = #{uuid}")
    void delete(String uuid);

    @Select("SELECT user_id FROM session where uuid = #{uuid}")
    int getIdBySession(String uuid);

    @Delete("delete from session")
    void deleteAll();
}
