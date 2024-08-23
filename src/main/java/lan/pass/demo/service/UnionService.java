package lan.pass.demo.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.model.Union;
import lan.pass.demo.mapper.UnionMapper;

import java.util.List;

@Service
public class UnionService{

    @Resource
    private UnionMapper unionMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return unionMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Union record) {
        return unionMapper.insert(record);
    }

    
    public int insertSelective(Union record) {
        return unionMapper.insertSelective(record);
    }

    
    public Union selectByPrimaryKey(Long id) {
        return unionMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Union record) {
        return unionMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Union record) {
        return unionMapper.updateByPrimaryKey(record);
    }

    public List<Union> findByOwnerId(Long ownerId) {
        return unionMapper.selectByOwnerId(ownerId);
    }
}
