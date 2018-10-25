package com.yousheng.yousheng;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yousheng.yousheng.habit.Habit;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.Calendar;
import java.util.Locale;


public class Application extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化litepal配置
        LitePal.initialize(this);
        ARouter.init(this);
        ARouter.openLog();
        ARouter.openDebug();
        ApplicationContextHolder.setApplicationContext(this);
        insertHabitData();
    }

    private void insertHabitData() {

        if (LitePal.count(Habit.class) > 0)
            return;

        {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Habit wakeUp = new Habit();
            wakeUp.setMainTitle("早起");
            wakeUp.setSubTitle("精神充沛，向新的一天问好。");
            wakeUp.setLevel(1);
            wakeUp.setOfficial(true);
            wakeUp.setNeedSign(true);
            wakeUp.setClockTime(calendar.getTimeInMillis());
            wakeUp.setContent("最佳起床时间：05:00~07:00，6：00最优。\n" +
                    "晚起床危害：\n" +
                    "1、精神状态差\n" +
                    "俗话说一年之计在于春，一日之计在于晨。研究表明，早起者可以“晒晒”晨光，情绪更积极，自我健康感更好。\n" +
                    "2、皮肤变差，黑眼圈变多\n" +
                    "长期早起，身体新陈代谢会很顺畅，皮肤会变好，就连黑眼圈也会变淡。\n" +
                    "3、患上抑郁症的风险大\n" +
                    "时间不充裕，容易制造焦虑情绪。早起者患上抑郁的风险更小。\n" +
                    "4、易长胖，身材走样\n" +
                    "作息不规律会导致内分泌失调，易发胖。\n" +
                    "5、免疫力降低，易患病\n" +
                    "多项研究表明，睡眠不足会增加患病率。早起可增强免疫力，帮助身体抗击感冒等病毒侵入。\n" +
                    "6、注意力分散，效率低\n" +
                    "研究证实，早起者字谜测试成绩高出30%。良好作息可以改善大脑的记忆力与注意力，帮助人们更顺利地解决复杂的问题。\n" +
                    "7、易负面情绪\n" +
                    "研究发现，作息不规律会增加暴躁、易怒、悲伤等负面情绪，也更易和他人出现误解及争吵。");
            wakeUp.save();

            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            Habit weight = new Habit();
            weight.setMainTitle("记录体重");
            weight.setSubTitle("体重超标不易怀孕，自律才能自强。");
            weight.setLevel(2);
            weight.setNeedSign(true);
            weight.setOfficial(true);
            weight.setClockTime(calendar.getTimeInMillis());
            weight.setContent("肥胖危害：\n" +
                    "1、影响激素平衡，不易怀孕\n" +
                    "导致内分泌紊乱，无排卵月经、月经稀少、闭经的比例远高于正常女性，排卵困难。\n" +
                    "2、影响卵子发育\n" +
                    "胖妈妈体内的胰岛素特别多，而胰岛素会影响女性生殖细胞的生长和发育，易导致卵子发育不成熟或者不健康，质量降低。宝宝出生后患上呼吸道疾病和腹泻的几率也会明显增加。\n" +
                    "3、孕期易引发综合症\n" +
                    "胖妈妈孕期出现妊娠糖尿病、妊娠高血压、脂肪肝、流产等问题的比例明显提高。\n" +
                    "4、分娩易难产\n" +
                    "胖妈妈非常容易怀上巨大儿，易发生难产，引起新生儿锁骨骨折、臂丛神经损伤及面神经瘫痪，如果产程时间过长，还可能使新生儿窒息，增加新生儿死亡率和后遗症。同时，还有可能发生准妈会阴及阴道的撕裂伤，产后子宫收缩乏力，引起产后大出血或产褥感染等并发症。如果采用剖腹产，由于脂肪层过厚，容易导致切口脂肪液化，伤口不容易愈合。");
            weight.save();

            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Habit habit = new Habit();
            habit.setMainTitle("补充叶酸");
            habit.setSubTitle("提前3个月补充，预防畸形儿，先天性心脏病等疾病。");
            habit.setLevel(3);
            habit.setNeedSign(true);
            habit.setOfficial(true);
            habit.setClockTime(calendar.getTimeInMillis());
            habit.setContent("最佳服用时间：饭后半小时服用，吸收更好。\n" +
                    "服用周期：怀孕前的3个月就必须要吃叶酸，一直吃到怀孕后的3个月底，一共是服用6个月。每天0.4毫克。\n" +
                    "不吃叶酸危害：\n" +
                    "畸形儿、先天性心脏病、兔唇等疾病的发生率将大幅变高。");
            habit.save();

            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Habit sprot = new Habit();
            sprot.setMainTitle("运动");
            sprot.setSubTitle("增强身体素质，促进卵子活力。");
            sprot.setLevel(4);
            sprot.setOfficial(true);
            sprot.setNeedSign(true);
            sprot.setClockTime(calendar.getTimeInMillis());
            sprot.setContent("最佳锻炼时间：至少30分钟，最佳1小时，不要超过2小时。\n" +
                    "不运动危害：");
            sprot.save();
        }

    }
}
