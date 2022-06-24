package cn.edu.xmu.mini.goods.config;

import cn.edu.xmu.mini.goods.model.Goods;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * 用户信息默认值设置拦截器
 * @author wl
 */
@Component
public class GoodsFieldFillInterceptor extends AbstractMongoEventListener<Goods> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Goods> event) {
        Goods goods = event.getSource();
        if (goods.getMonthSale() == null) {
            goods.setMonthSale(0);
        }
        if (goods.getState() == null) {
            goods.setState(0);
        }
    }
}
