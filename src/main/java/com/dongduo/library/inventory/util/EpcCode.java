package com.dongduo.library.inventory.util;

import org.springframework.util.Assert;

import java.util.StringJoiner;

/**
 * EPC号
 */
public class EpcCode {
    private static int ITEM_ID_LENGTH = 13;

    /**
     * 借出标志
     * EPC 号以 LENDING_FLAG_TRUE 结尾时表示已借出
     * EPC 号以 LENDING_FLAG_FALSE 结尾时表示未借出
     */
    private static final String LENDING_FLAG_TRUE = "00";
    private static final String LENDING_FLAG_FALSE = "F7";

    /**
     * 是否已借出
     */
    private boolean lending;

    /**
     * 二进制编码方式
     * 值定义如下:
     * 00:编码方式1,EPC 容量为 96bit (12Byte)
     * 01:编码方式2,EPC 容量为 128bit (16Byte)
     * 10:编码方式3,EPC 容量为 144bit 或以上 (≥18Byte)
     * 11:编码方式4,保留(暂无定义)
     */
    private String encodingTypeBinary = "10";

    /**
     * 二进制数据模型版本
     * 二进制为6bit,从000000开始,即000000对应的版本为1,000001对应的版本为2,依此类推
     */
    private String versionBinary = "000011";

    /**
     * 馆藏标识符
     * 通常为自定义条码号
     */
    private String itemId;

    public EpcCode() {
    }

    /**
     * 构造方法
     * @param lending 是否已借出
     * @param itemId 馆藏标识符,通常为自定义条码号
     */
    public EpcCode(boolean lending, String itemId) {
        this.lending = lending;
        this.itemId = itemId;
    }

    public boolean isLending() {
        return lending;
    }

    public void setLending(boolean lending) {
        this.lending = lending;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getEncodingType() {
        return Integer.parseInt(encodingTypeBinary, 2) + 1;
    }

    public int getVersion() {
        return Integer.parseInt(versionBinary, 2) + 1;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EpcCode.class.getSimpleName() + "[", "]")
                .add("lending=" + lending)
                .add("encodingTypeBinary='" + encodingTypeBinary + "'")
                .add("versionBinary='" + versionBinary + "'")
                .add("itemId='" + itemId + "'")
                .toString();
    }

    /**
     * 转换为16进制字符串,即写入标签的EPC号
     * 兼容EAS仿真报警和借出标志位报警两种模式
     * 只需要调整通道门报警设置即可切换两种报警模式
     */
    public String toHexString() {
        StringBuilder hex = new StringBuilder();
        // 兼容全国高校联盟协议，使用通道门EAS仿真报警
        hex.append(formatHex(Integer.toHexString(Integer.parseInt(lending ? "10000000" : "00000000", 2))));
        hex.append(formatHex(Integer.toHexString(Integer.parseInt(encodingTypeBinary + versionBinary, 2))));
        hex.append("0000");
        char[] itemIdCharArray = itemId.toCharArray();
        int itemIdLength = itemIdCharArray.length;
        for (int i = 0; i < ITEM_ID_LENGTH; i ++) {
            if (i + itemIdLength < ITEM_ID_LENGTH) {
                hex.append("00");
            } else {
                hex.append(formatHex(Integer.toHexString(itemIdCharArray[i - ITEM_ID_LENGTH + itemIdLength])));
            }
        }
        // 兼容自定义报警协议，使用工控机判断借出标志位报警
        hex.append(lending ? LENDING_FLAG_TRUE : LENDING_FLAG_FALSE);
        return hex.toString();
    }

    /**
     * 将16进制字符串解析为EpcCode对象
     * @param hexString 从标签读出的16进制EPC号字符串
     */
    public static EpcCode parseEpcCode(String hexString) {
        Assert.hasText(hexString, "EPC号不能为空。");
        if (hexString.length() != 36) {
            throw new IllegalArgumentException("EPC号必须是36位。");
        }

        StringBuilder itemIdBuilder = new StringBuilder();
        char[] hexCharArray = hexString.toCharArray();
        for (int i = 8; i < hexString.length() - 2; i = i + 2) {
            String subHex = hexCharArray[i] + "" + hexCharArray[i + 1];
            if (!"00".equals(subHex)) {
                itemIdBuilder.append((char) Integer.parseInt(subHex, 16));
            }
        }
        boolean lending = hexString.endsWith(LENDING_FLAG_TRUE) || hexString.endsWith(LENDING_FLAG_TRUE.toLowerCase());
        return new EpcCode(lending, itemIdBuilder.toString());
    }

    private String formatHex(String s) {
        return s.length() == 1 ? "0" + s : s;
    }

    public static void main(String[] args) {
        EpcCode epcCode = new EpcCode(true, "121000187");
        System.out.println(epcCode.toHexString());
        System.out.println(epcCode.toString());
    }
}
