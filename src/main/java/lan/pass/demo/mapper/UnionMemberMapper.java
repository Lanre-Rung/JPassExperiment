package lan.pass.demo.mapper;

import lan.pass.demo.model.Merchant;
import lan.pass.demo.model.Union;
import lan.pass.demo.model.UnionMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UnionMemberMapper {
    int deleteByPrimaryKey(@Param("unionId") Long unionId, @Param("merchantId") Long merchantId);

    int insert(UnionMember record);

    int insertSelective(UnionMember record);

    UnionMember selectByPrimaryKey(@Param("unionId") Long unionId, @Param("merchantId") Long merchantId);

    int updateByPrimaryKeySelective(UnionMember record);

    int updateByPrimaryKey(UnionMember record);

    List<Union> selectUnionsByMemberId(Long memberId);

    List<Merchant> selectMerchantByUnionId(Long unionId);
}