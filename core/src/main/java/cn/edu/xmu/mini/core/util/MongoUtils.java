package cn.edu.xmu.mini.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;

@Slf4j
public class MongoUtils {
    public static Update getUpdateByObj(Object obj) {
        Update update = new Update();
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    update.set(field.getName(), value);
                }
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }

        return update;
    }
}
