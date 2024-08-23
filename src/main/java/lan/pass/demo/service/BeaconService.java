package lan.pass.demo.service;

import lan.pass.demo.mapper.ChangeMessageMapper;
import lan.pass.demo.model.ChangeMessage;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.model.Beacon;
import lan.pass.demo.mapper.BeaconMapper;
@Service
public class BeaconService{

    @Resource
    private BeaconMapper beaconMapper;
    @Resource
    private ChangeMessageMapper changeMessageMapper;
    
    public int deleteByPrimaryKey(Long id) {
        return beaconMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Beacon record) {
        return beaconMapper.insert(record);
    }

    
    public int insertSelective(Beacon record) {
        return beaconMapper.insertSelective(record);
    }

    
    public Beacon selectByPrimaryKey(Long id) {
        return beaconMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Beacon record) {
        return beaconMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Beacon record) {
        return beaconMapper.updateByPrimaryKey(record);
    }

}
