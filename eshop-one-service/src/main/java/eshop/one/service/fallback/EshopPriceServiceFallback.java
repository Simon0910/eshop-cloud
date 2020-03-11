package eshop.one.service.fallback;

import eshop.one.service.EshopPriceService;
import org.springframework.stereotype.Component;

/**
 * @author lzp on 2020/3/11.
 */
@Component
public class EshopPriceServiceFallback implements EshopPriceService {
    @Override
    public String findByProductId(Long productId) {
        return "";
    }
}
