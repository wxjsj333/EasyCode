package com.sjhy.plugin.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类型隐射信息
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
@Data
@NoArgsConstructor
public class ServiceModule {
    /**
     * 列类型
     */
    private String name;

    public ServiceModule(String name) {
        this.name = name;
    }
}
