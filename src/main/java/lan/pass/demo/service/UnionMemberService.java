package lan.pass.demo.service;

import lan.pass.demo.mapper.UnionMapper;
import lan.pass.demo.model.Merchant;
import lan.pass.demo.model.Union;
import lombok.var;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.model.UnionMember;
import lan.pass.demo.mapper.UnionMemberMapper;

import java.util.List;

@Service
public class UnionMemberService{

    @Resource
    private UnionMemberMapper unionMemberMapper;

    @Resource
    private UnionMapper unionMapper;

    @Resource
    private MerchantService merchantService;
    
    public int deleteByPrimaryKey(Long unionId,Long merchantId) {
        return unionMemberMapper.deleteByPrimaryKey(unionId,merchantId);
    }
    
    public Object insert(UnionMember record) {
        if (record.getUnionId() == null || record.getMerchantId() == null){
            return "请填入完整的数据";
        }
        Union union = unionMapper.selectByPrimaryKey(record.getUnionId());
        if (union.getOwnerId() == record.getMerchantId()){
            return "你是联盟的创建者，请不要把自己再加入到联盟里";
        }
        try {
            return unionMemberMapper.insert(record);
        } catch (DuplicateKeyException e) {
            return "请勿重复添加";
        }
    }
    
    public int insertSelective(UnionMember record) {
        return unionMemberMapper.insertSelective(record);
    }

    
    public UnionMember selectByPrimaryKey(Long unionId,Long merchantId) {
        return unionMemberMapper.selectByPrimaryKey(unionId,merchantId);
    }

    
    public int updateByPrimaryKeySelective(UnionMember record) {
        return unionMemberMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(UnionMember record) {
        return unionMemberMapper.updateByPrimaryKey(record);
    }

    public List<Union> selectUnionsByMemberId(Long memberId, boolean includingOwner){
        List<Union> other = unionMemberMapper.selectUnionsByMemberId(memberId);
        if (includingOwner){
            other.addAll(unionMapper.selectByOwnerId(memberId));
        }
        return other;
    }

    public List<Merchant> selectMerchantByUnionId(Long unionId, boolean includingOwner){
        List<Merchant> other = unionMemberMapper.selectMerchantByUnionId(unionId);
        if (includingOwner){
             Union union = unionMapper.selectByPrimaryKey(unionId);
             Merchant merchant = merchantService.getMerchantById(union.getOwnerId());
            other.add(0, merchant);
        }
        return other;
    }
}
