package lan.pass.demo.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.mapper.RelevantDateMapper;
import lan.pass.demo.model.RelevantDate;
@Service
public class RelevantDateService{

    @Resource
    private RelevantDateMapper relevantDateMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return relevantDateMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(RelevantDate record) {
        return relevantDateMapper.insert(record);
    }

    
    public int insertSelective(RelevantDate record) {
        return relevantDateMapper.insertSelective(record);
    }

    
    public RelevantDate selectByPrimaryKey(Long id) {
        return relevantDateMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(RelevantDate record) {
        return relevantDateMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(RelevantDate record) {
        return relevantDateMapper.updateByPrimaryKey(record);
    }

}
