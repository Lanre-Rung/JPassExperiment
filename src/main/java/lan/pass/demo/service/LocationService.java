package lan.pass.demo.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.mapper.LocationMapper;
import lan.pass.demo.model.Location;
@Service
public class LocationService{

    @Resource
    private LocationMapper locationMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return locationMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Location record) {
        return locationMapper.insert(record);
    }

    
    public int insertSelective(Location record) {
        return locationMapper.insertSelective(record);
    }

    
    public Location selectByPrimaryKey(Long id) {
        return locationMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Location record) {
        return locationMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Location record) {
        return locationMapper.updateByPrimaryKey(record);
    }

}
