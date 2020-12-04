package in.auto.jira.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算类型枚举
 * 
 * @author yinjun(yinjunonly@163.com)
 * @date 2020-08-24 21:52:30
 */
@Getter
@AllArgsConstructor
public enum SettlementType {
    RATE(1, "扣量"), FIXED(2, "固价");

    private int type;
    private String name;

    /**
     * 是否为固价
     * 
     * @param type 类型编号
     * @return
     */
    public static boolean isFixed(int type) {
        return type == SettlementType.FIXED.type;
    }
}