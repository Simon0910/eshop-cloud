package eshop.one.service.fallback;

import eshop.one.service.EshopInventoryService;
import org.springframework.stereotype.Component;

/**
 * @author lzp on 2020/3/11.
 */
@Component
public class EshopInventoryServiceFallback implements EshopInventoryService {
    @Override
    public String findByProductId(Long productId) {
        return "";
    }
}
