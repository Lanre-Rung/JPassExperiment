package lan.pass.demo.mapper;

import lan.pass.demo.model.History;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryMapper {
    List<History> selectHistoryByUserId(@Param("userId") Long userId);
    List<History> selectHistoryByPassId(@Param("passId") Long passId);
    List<History> selectHistoryByBothIds(@Param("userId") Long userId, @Param("passId") Long passId);
    // Create a new history entry
    int insertHistory(History history);
    // Edit an existing history entry by ID
    int updateHistory(History history);
    // View a history entry by ID
    History selectHistoryById(Long id);
    // Delete a history entry by ID
    int deleteHistory(Long id);
}
