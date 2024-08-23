package lan.pass.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {
    private Long id;
    private Long operationId;
    private Long merchantId;
    private LocalDateTime date;
    private Long passId;
    private String operationType;
    public History(Long operationId, Long merchantId, LocalDateTime date, Long passId){
        this.operationId = operationId;
        this.merchantId = merchantId;
        this.date = date;
        this.passId = passId;
    }
}