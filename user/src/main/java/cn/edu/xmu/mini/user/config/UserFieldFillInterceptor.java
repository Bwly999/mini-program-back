package cn.edu.xmu.mini.user.config;

import cn.edu.xmu.mini.user.model.User;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户信息默认值设置拦截器
 * @author wl
 */
@Component
public class UserFieldFillInterceptor extends AbstractMongoEventListener<User> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<User> event) {
        User user = event.getSource();
        if (user.getAddressList() == null) {
            user.setAddressList(List.of());
        }
    }
}
