package com.example.yl_app.utils;

import android.content.Context;
import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.database.DrinkEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void initDefaultData(Context context) {
        executor.execute(() -> {
            DrinkDatabase db = DrinkDatabase.getInstance(context);
            if (db.drinkDao().getAllDrinks().isEmpty()) {
                // 奶茶
                db.drinkDao().insert(new DrinkEntity("云栖桂露", "奶茶", 12.0, "yunqiguilu", "桂花香里的一口温柔","人气爆款", true));
                db.drinkDao().insert(new DrinkEntity("紫汀芋糯", "奶茶", 13.0, "zidingyunuo", "满满芋泥，糯叽叽满足", "人气爆款",true));
                db.drinkDao().insert(new DrinkEntity("枕月乌龙奶", "奶茶", 8.0, "zhenyuewulongnai", "枕着月光入睡的奶香", "",false));
                db.drinkDao().insert(new DrinkEntity("石栖芍芬", "奶茶", 13.0, "shiqishaofen", "桑葚紫韵，花香入喉", "",false));
                db.drinkDao().insert(new DrinkEntity("晚棠红豆乳", "奶茶", 11.0, "wantanghongdouru", "红豆软糯，晚棠微甜","", false));
                db.drinkDao().insert(new DrinkEntity("青岚豆乳茶", "奶茶", 14.0, "qinglandourucha", "豆香清浅，一口治愈","",false));
                db.drinkDao().insert(new DrinkEntity("松月厚乳", "奶茶", 15.0, "songyuehouru", "海盐厚乳，咸香不腻", "",false));
                db.drinkDao().insert(new DrinkEntity("雾栖糯香奶", "奶茶", 16.0, "wuqinuoxiangnai", "血糯米嚼着喝才过瘾","", false));
                // 果茶
                db.drinkDao().insert(new DrinkEntity("月映荔枝", "果茶", 15.0, "yueyinglizhi", "荔枝的清甜，夏天的味道", "新品",true));
                db.drinkDao().insert(new DrinkEntity("香絮桃涧", "果茶", 14.0, "xiangxutaojian", "白桃香气，像咬了一口春天", "",false));
                db.drinkDao().insert(new DrinkEntity("芒屿碎金", "果茶", 16.0, "mangyusuijin", "台农芒果，甜到心里", "",false));
                db.drinkDao().insert(new DrinkEntity("落樱葡露", "果茶", 15.0, "luoyingpolu", "葡萄爆汁，清爽一夏", "",false));
                db.drinkDao().insert(new DrinkEntity("棠梨清柠", "果茶", 12.0, "tangliqingning", "柠檬的酸，棠梨的甜","", false));
                db.drinkDao().insert(new DrinkEntity("青提云涧", "果茶", 16.0, "qingtiyunjian", "青提脆甜，一口爆汁", "",false));
                db.drinkDao().insert(new DrinkEntity("荔香芒岚", "果茶", 17.0, "lixiangmanglan", "荔枝撞芒果，双重果香", "",false));
                db.drinkDao().insert(new DrinkEntity("杨梅枕夏", "果茶", 16.0, "yangmeizhenxia", "杨梅酸甜，解暑神器","", false));
                db.drinkDao().insert(new DrinkEntity("栀香莓屿", "果茶", 13.0, "zhixiangmeiyu", "草莓配栀子，甜妹首选", "",false));
                db.drinkDao().insert(new DrinkEntity("金桔云舒", "果茶", 9.0, "jinjuyunshu", "金桔清爽，解腻必备", "",false));
                // 小料
                db.drinkDao().insert(new DrinkEntity("黑糖珍珠", "小料", 1.0, "heitangzhenzhu", "Q弹有嚼劲，黑糖焦香", "",false));
                db.drinkDao().insert(new DrinkEntity("椰果", "小料", 1.0, "yeguo", "清甜脆爽，百搭不出错", "",false));
                db.drinkDao().insert(new DrinkEntity("小西米", "小料", 1.0, "xiaoximi", "滑溜溜，吸着喝更过瘾", "",false));
                db.drinkDao().insert(new DrinkEntity("红豆", "小料", 1.0, "hongdou", "蜜渍红豆，软糯香甜", "",false));
                db.drinkDao().insert(new DrinkEntity("仙草冻", "小料", 1.0, "xiancaodong", "清凉解暑，滑嫩Q弹","", false));
                db.drinkDao().insert(new DrinkEntity("糯米麻薯", "小料", 2.0, "nuomimashu", "拉丝麻薯，糯叽叽星人最爱", "",false));
                db.drinkDao().insert(new DrinkEntity("血糯米", "小料", 2.0, "xuenuomi", "饱腹感强，越嚼越香", "",false));
                // 冰淇淋
                db.drinkDao().insert(new DrinkEntity("紫薯香芋冰淇淋", "冰淇淋", 3.0, "zishuxiangyubingqiling", "香芋控的快乐星球","新品", true));
                db.drinkDao().insert(new DrinkEntity("香草牛乳冰淇淋", "冰淇淋", 3.0, "xiangcaoniurubingqiling", "经典香草，怎么吃都不腻","", false));
                // 纯茶
                db.drinkDao().insert(new DrinkEntity("松涧乌龙", "纯茶", 7.0, "songjianwulong", "高山乌龙，岩韵悠长", "人气爆款",true));
                db.drinkDao().insert(new DrinkEntity("青岚龙井", "纯茶", 7.0, "qinglanlongjing", "龙井鲜爽，一口回甘", "",false));
                db.drinkDao().insert(new DrinkEntity("云栖桂茶", "纯茶", 7.0, "yunqiguicha", "冷泡桂花，平价好喝", "",false));
            }
        });
    }
}