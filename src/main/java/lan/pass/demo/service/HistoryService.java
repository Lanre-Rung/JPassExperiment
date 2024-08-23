package lan.pass.demo.service;

import lan.pass.demo.mapper.HistoryMapper;
import lan.pass.demo.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    private HistoryMapper historyMapper;

    @Autowired
    public void setHistoryMapper(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    public List<History> searchHistory(Long userId, Long passId) {
        if (userId != null && passId == null) {
            return historyMapper.selectHistoryByUserId(userId);
        } else if (userId == null && passId != null) {
            return historyMapper.selectHistoryByPassId(passId);
        } else {
            return historyMapper.selectHistoryByBothIds(userId, passId);
        }
    }
    public History createHistory(History history) {
        historyMapper.insertHistory(history);
        return history;
    }

    public History editHistory(Long id, History updatedHistory) {
        updatedHistory.setId(id);
        historyMapper.updateHistory(updatedHistory);
        return updatedHistory;
    }

    public History viewHistory(Long id) {
        return historyMapper.selectHistoryById(id);
    }

    public void deleteHistory(Long id) {
        historyMapper.deleteHistory(id);
    }

}