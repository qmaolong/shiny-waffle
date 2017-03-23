package com.covilla.common;

/**
 * Created by qmaolong on 2016/10/12.
 */
public class TicketParam {

    /**
     * 打印区域
     */
    public enum TicketZone {
        Head(0, ""),Content(1, ""),Tail(2, "");
        public Integer code;
        public String name;

        TicketZone(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 小数位
     */
    public enum TicketDecimal{
        Decimal0 (0, "无小数位"),
        Decimal1 (1, "1位小数"),
        Decimal2 (2, "2位小数"),
        Decimal3 (3, "3位小数"),
        Decimal4 (4, "4位小数");
        public Integer code;
        public String name;

        TicketDecimal(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 文字截断
     */
    public enum TicketOverflow {
        NoTrim(0, "不截断"),
        TrimLeft(1, "左边截断"),
        TrimRight(2, "右边截断");

        public Integer code;
        public String name;

        TicketOverflow(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 文字样式
     */
    public enum TicketTextStyle {
        Normal(0, "正常"),
        Italic(1, "斜体"),
        Bold(2, "粗体"),
        BoldItalic(3, "粗斜体");

        public Integer code;
        public String name;

        TicketTextStyle(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 文字大小
     */
    public enum TicketTextSize {
        Normal(0, "正常"),
        DoubleWidth(1, "倍宽"),
        DoubleHeight(2, "倍高"),
        DoubleWidthAndHeight(3, "倍宽&倍高");
        public Integer code;
        public String name;

        TicketTextSize(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 项类型
     */
    public enum TicketItemType
    {
        NORMAL(0, "数据项"),
        USER(1, "自定义"),
        SEPARATOR(2, "分割线");
        public Integer code;
        public String name;

        TicketItemType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 填充空格
     */
    public enum TicketFillType
    {
        None(0, "不填充"),
        Left(1, "向左"),
        Right(2, "向右"),
        Center(3, "居中");
        public Integer code;
        public String name;

        TicketFillType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    /**
     * 打印类型
     */
    public enum TicketCategory
    {
        Ticket(0, "小票"),
        Kitchen(1, "厨打"),
        Control(2, "控菜"),
        Takeout(3, "外卖");
        public Integer code;
        public String name;

        TicketCategory(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    public static void main(String[] args){
        System.out.print(TicketZone.Head.code);
    }
}
