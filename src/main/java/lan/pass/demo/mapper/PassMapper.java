package lan.pass.demo.mapper;

import lan.pass.demo.model.Pass;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PassMapper {

    @Insert("INSERT INTO passes (name, owner_id) VALUES (#{name}, #{ownerId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPass(Pass pass);

    @Select("SELECT * FROM passes WHERE id = #{id}")
    Pass selectPassById(Long id);

    @Select("SELECT * FROM passes")
    List<Pass> selectAllPasses();

    @Update("UPDATE passes SET name = #{name}, owner_id = #{ownerId} WHERE id = #{id}")
    void updatePass(Pass pass);

    @Delete("DELETE FROM passes WHERE id = #{id}")
    void deletePass(Long id);
}