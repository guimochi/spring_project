package vinci.stock.execution.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import vinci.stock.execution.dto.FilledUpdateRequest;

@Repository
@FeignClient(name = "order")
public interface OrderProxy {

    @GetMapping("/order/{guid}")
    void getOne(@PathVariable String guid);

    @PatchMapping("/order/{guid}")
    void updateOne(@PathVariable String guid, @RequestBody FilledUpdateRequest filled);
}
