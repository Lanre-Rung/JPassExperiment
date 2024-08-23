package lan.pass.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.brendamour.jpasskit.PKField;
import lan.pass.demo.config.PathConfig;
import lan.pass.demo.utility.FileFetcher;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.mapper.FieldMapper;
import lan.pass.demo.model.Field;

import java.io.IOException;

@Service
public class FieldService{

    @Resource
    private FieldMapper fieldMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return fieldMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(Field record) {
        return fieldMapper.insert(record);
    }
    
    public int insertSelective(Field record) {
        int affectedRows = fieldMapper.insertSelective(record);
        FileFetcher fileFetcher = new FileFetcher();
        try {
            fileFetcher.createFile(record.getFileContent(), "field\\" + record.getId(), "json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    
    public Field selectByPrimaryKey(Long id) {
        return fieldMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Field record) {
        FileFetcher fileFetcher = new FileFetcher();
        try {
            fileFetcher.createFile(record.getFileContent(), "field\\" + record.getId(), "json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    
    public int updateByPrimaryKey(Field record) {
        return fieldMapper.updateByPrimaryKey(record);
    }

    public void getPKField(Field record){
        if (record.getId() == null){
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String fileName =  + record.getId() + ".json";
        JsonNode node = FileFetcher.readFile(PathConfig.field + fileName);
        record.setFileContent(node.get(fileName));
    }
}
