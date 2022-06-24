package cn.edu.xmu.mini.orders.config;

import cn.edu.xmu.mini.orders.model.Orders;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * 用户信息默认值设置拦截器
 * @author wl
 */
@Component
public class OrdersFieldFillInterceptor extends AbstractMongoEventListener<Orders> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Orders> event) {
        Orders orders = event.getSource();
        if (orders.getState() == null) {
            orders.setState(0);
        }
    }
}
