package com.aizuda.trans.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应结果
 *
 * @author nn200433
 * @date 2023-05-25 025 14:47:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<R> {

    /** 状态 */
    private Integer status;

    /** 数据 */
    private R data;

}
