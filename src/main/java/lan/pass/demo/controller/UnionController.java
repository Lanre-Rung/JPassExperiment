package lan.pass.demo.controller;

import lan.pass.demo.model.AssetType;
import lan.pass.demo.model.Merchant;
import lan.pass.demo.model.UnionMember;
import lan.pass.demo.request.UnionAssetSearch;
import lan.pass.demo.service.UnionAssetService;
import lan.pass.demo.service.UnionMemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lan.pass.demo.model.Union;
import lan.pass.demo.service.UnionService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/union") // Base URL for Union resources
public class UnionController {

    @Resource
    private UnionService unionService;
    @Resource
    private UnionMemberService unionMemberService;
    @Resource
    private UnionAssetService unionAssetService;

    // GET Unions by owner_id
    @GetMapping("/owner_id/{id}")
    public ResponseEntity<List<Union>> getUnionsByOwnerId(@PathVariable Long id) {
        List<Union> unions = unionService.findByOwnerId(id);
        if (unions == null || unions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unions);
    }

    // GET Unions by merchant_id
    @GetMapping("/merchant_id/{id}")
    public ResponseEntity<List<Union>> getUnionsByMerchantId(@PathVariable Long id, @RequestParam(value = "includingOwner", required = false, defaultValue = "true") boolean includingOwner) {
        List<Union> unions = unionMemberService.selectUnionsByMemberId(id, includingOwner);
        if (unions == null || unions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unions);
    }

    // GET a single Union by ID
    @GetMapping("/{id}")
    public ResponseEntity<Union> getUnionById(@PathVariable Long id) {
        Union union = unionService.selectByPrimaryKey(id);
        if (union == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(union);
    }

    // POST a new Union
    @PostMapping
    public ResponseEntity<Union> createUnion(@RequestBody Union union) {
        int result = unionService.insertSelective(union);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(union);
    }

    // PUT to update an existing Union
    @PutMapping
    public ResponseEntity<Union> updateUnion(@RequestBody Union union) {
        Union toUpdate = unionService.selectByPrimaryKey(union.getId());
        if (toUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        int result = unionService.updateByPrimaryKeySelective(union);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(union);
    }

    // DELETE an existing Union by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnion(@PathVariable Long id) {
        int result = unionService.deleteByPrimaryKey(id);
        if (result == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header("Deleted-Id", String.valueOf(id)).build();
    }

    @PostMapping("/member")
    public Object addUnionMember(@RequestBody UnionMember record) {
        return unionMemberService.insert(record);
    }

    @DeleteMapping("/member")
    public Object deleteUnionMember(@RequestBody UnionMember record) {
        if (record.getUnionId() != null && record.getMerchantId() != null ){
            return unionMemberService.deleteByPrimaryKey(record.getUnionId(), record.getMerchantId());
        }
        return "请输入完整的数据";
    }

    // GET Unions by merchant_id
    @GetMapping("/member/{id}")
    public ResponseEntity<List<Merchant>> selectMerchantByUnionId(@PathVariable Long id, @RequestParam(value = "includingOwner", required = false, defaultValue = "true") boolean includingOwner) {
        List<Merchant> merchants = unionMemberService.selectMerchantByUnionId(id, includingOwner);
        if (merchants == null || merchants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(merchants);
    }

    // GET endpoint to fetch all union assets
    @PostMapping("/asset/union_id")
    public Object getUnionAssetsByUnionId(@RequestBody UnionAssetSearch search) {
        return unionAssetService.selectAssetByUnionId(search);
    }
}