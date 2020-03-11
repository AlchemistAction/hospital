package net.thumbtack.school.hospital.mybatis.mappers;


import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.annotations.*;



public interface AdminMapper {

    @Insert("INSERT INTO admin ( firstName, lastName, patronymic, position, login, password) VALUES"
            + "(#{admin.firstName}, #{admin.lastName}, #{admin.patronymic}, #{admin.position}, #{admin.login}" +
            ", #{admin.password})")
    @Options(useGeneratedKeys = true, keyProperty = "admin.id")
    Integer insert(@Param("admin") Admin admin);


    @Select("SELECT id, firstName, lastName, patronymic, position, login, password FROM admin WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
    })
    Admin getById(int id);
//
//    @Update("UPDATE trainee SET lastName = #{lastName} WHERE id = #{trainee.id} ")
//    void changeLastName(@Param("trainee") Trainee trainee, @Param("lastName") String lastName);
//
//    @Delete("DELETE FROM trainee WHERE id = #{trainee.id}")
//    void delete(@Param("trainee") Trainee trainee);
//
//    @Select("SELECT id, firstname, lastname, rating FROM trainee")
//    @Results({
//            @Result(property = "id", column = "id"),
//    })
//    List<Trainee> getAllLazy();
//
    @Delete("DELETE FROM admin")
    void deleteAll();
//
//    @Select({"<script>",
//            "SELECT id, firstName, lastName, rating FROM trainee",
//            "<where>" +
//                    "<if test='firstName != null'> firstName like #{firstName}",
//            "</if>",
//            "<if test='lastName != null'> AND lastName like #{lastName}",
//            "</if>",
//            "<if test='rating != null'> AND rating like #{rating}",
//            "</if>",
//            "</where>" +
//                    "</script>"})
//    @Results({
//            @Result(property = "id", column = "id"),
//    })
//    List<Trainee> getAllWithParams(@Param("firstName") String firstName, @Param("lastName") String lastName,
//                                   @Param("rating") Integer rating);
//
//    @Insert({"<script>",
//            "INSERT INTO trainee (firstname, lastname, rating) VALUES",
//            "<foreach item='item' collection='list' separator=','>",
//            "( #{item.firstName}, #{item.lastName}, #{item.rating} )",
//            "</foreach>",
//            "</script>"})
//    @Options(useGeneratedKeys = true)
//    void batchInsert(@Param("list") List<Trainee> trainees);
//
//    @Select("SELECT * FROM trainee WHERE groupid = #{group.id}")
//    List<Trainee> getByGroup(Group group);


}
