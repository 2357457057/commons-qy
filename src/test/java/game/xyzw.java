package game;

import cn.hutool.core.swing.RobotUtil;

import java.awt.*;

public class xyzw {

    static double[] BOTTOM_BTN1 = new double[]{2032.0, 1050.0};
    static double[] BOTTOM_BTN2 = new double[]{2151.0, 1050.0};
    static double[] BOTTOM_BTN3 = new double[]{2265.0, 1050.0};
    static double[] BOTTOM_BTN4 = new double[]{2373.0, 1050.0};
    static double[] BOTTOM_BTN5 = new double[]{2489.0, 1050.0};


    static double[] Goto_Fire = new double[]{2497.0, 118.0};

    static double[] CENTER_CLICK = new double[]{2264.0, 577.0};

    static double[] Wooden_box = new double[]{2034.0, 906.0};
    static double[] Copper_box = new double[]{2151.0, 919.0};
    static double[] Gold_box = new double[]{2267.0, 923.0};
    static double[] Platinum_box = new double[]{2379.0, 924.0};
    static double[] Diamond_box = new double[]{2496.0, 923.0};
    static double[] Open_box = new double[]{2271.0, 767.0};
    static double[] Open_box_ok = new double[]{1996.0, 730.0};
    static double[] Get_box = new double[]{2478.0, 298.0};
    static double[] Get_box_ok = new double[]{2259.0, 850.0};


    static double[] on_hook = new double[]{2018.0, 445.0};
    static double[] on_hook1 = new double[]{2355.0, 885.0};
    static double[] on_hook2 = new double[]{2224.0, 891.0};

    public static void main(String[] args) throws AWTException, InterruptedException {
        while (true) {
            //主菜单
            clickRepeat(BOTTOM_BTN3, 1);
            Thread.sleep(2000);

            //助战
            clickRepeat(CENTER_CLICK, 900000);
            Thread.sleep(1800000);

            //挂机材料
            onHoke$();

            //开箱子
            openBox();
            openBox();
        }
    }

    /**
     * 开箱子
     */
    static void openBox() throws InterruptedException {
        clickRepeat(Goto_Fire, 2);
        Thread.sleep(2000);

        clickRepeat(BOTTOM_BTN4, 2);
        Thread.sleep(2000);

        clickRepeat(Wooden_box, 1);
        Thread.sleep(200);
        clickRepeat(Open_box, 1);
        Thread.sleep(3000);
        clickRepeat(Open_box_ok, 3);
        Thread.sleep(1000);

        clickRepeat(Copper_box, 1);
        Thread.sleep(200);
        clickRepeat(Open_box, 1);
        Thread.sleep(3000);
        clickRepeat(Open_box_ok, 3);
        Thread.sleep(1000);

        clickRepeat(Gold_box, 1);
        Thread.sleep(200);
        clickRepeat(Open_box, 1);
        Thread.sleep(3000);
        clickRepeat(Open_box_ok, 3);
        Thread.sleep(1000);

        clickRepeat(Platinum_box, 1);
        Thread.sleep(200);
        clickRepeat(Open_box, 1);
        Thread.sleep(3000);
        clickRepeat(Open_box_ok, 3);
        Thread.sleep(1000);

        clickRepeat(Diamond_box, 1);
        Thread.sleep(200);
        clickRepeat(Open_box, 1);
        Thread.sleep(3000);
        clickRepeat(Open_box_ok, 3);
        Thread.sleep(1000);

        clickRepeat(Get_box, 3);
        Thread.sleep(700);
        clickRepeat(Get_box_ok, 1);
        Thread.sleep(2000);
    }

    /**
     * 挂机材料
     */
    static void onHoke$() throws InterruptedException {
        clickRepeat(on_hook, 1);
        Thread.sleep(2000);
        clickRepeat(on_hook1, 10);
        Thread.sleep(2000);
        clickRepeat(on_hook2, 1);
        Thread.sleep(2000);

    }

    public static void clickRepeat(double[] btn, int repeat) {
        RobotUtil.setDelay(1);
        RobotUtil.mouseMove((int) btn[0], (int) btn[1]);
        for (int i = 0; i < repeat; i++) {
            RobotUtil.click();
        }
    }

    static double[] getMouseLocation() {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        Point location = pointerInfo.getLocation();
        return new double[]{location.getX(), location.getY()};
    }
}
