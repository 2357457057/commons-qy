import top.yqingyu.common.utils.VirtualConsoleTable;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName PACKAGE_NAME.TestTable
 * @description
 * @createTime 2023年02月19日 03:30:00
 */
public class TestTable {


    public static void main(String[] args) {
        VirtualConsoleTable table = new VirtualConsoleTable();
        table.append("aa", "aaa", "aaaa", "aaaaa").newLine()
                .append("bbbbbb").appendCrossRow("CrossRow", 2).newLine()
                .append("cccccccccccccccccccccccc").append("ccccccccccc").newLine()
                .append("ddddddddddddddddddddddddd").appendCrossCol("CrossCol", 2).append("dddddddddd").newLine();
        System.out.println(table);

        System.out.println("===========================================================================");

        table.put("put1", 3, 1);
        System.out.println(table);

        System.out.println("===========================================================================");
        table.put("put2", 2, 2);
        System.out.println(table);
    }
}
