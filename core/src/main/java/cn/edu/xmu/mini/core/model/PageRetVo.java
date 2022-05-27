package cn.edu.xmu.mini.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRetVo<T> {
    private int current;
    private int page;
    private int pages;
    private long total;
    private T content;
}
