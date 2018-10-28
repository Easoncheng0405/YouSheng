package com.yousheng.yousheng;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yousheng.yousheng.model.Market;
import com.yousheng.yousheng.model.Habit;

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
        insertMarketData();
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

            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Habit sprot = new Habit();
            sprot.setMainTitle("运动");
            sprot.setSubTitle("增强身体素质，促进卵子活力。");
            sprot.setLevel(3);
            sprot.setOfficial(true);
            sprot.setNeedSign(true);
            sprot.setClockTime(calendar.getTimeInMillis());
            sprot.setContent("最佳锻炼时间：至少30分钟，最佳1小时，不要超过2小时。\n" +
                    "最佳锻炼时间：至少30分钟，最佳1小时，不要超过2小时。\n" +
                    "缺乏运动的危害：\n" +
                    "1.畸形儿、先天性心脏病、兔唇等疾病的发生率将大幅变高。\n" +
                    "2.使胸腔血液不足，导致人的心、肺功能进一步降低。\n" +
                    "3.长期不动会引发全身肌肉酸痛、脖子僵硬和头痛头晕。\n" +
                    "4.容易引起肠胃蠕动减慢，消化腺分泌消化液减少，出现食欲不振等症状，加重人的腹胀、便秘、消化不良等消化系统症状。\n" +
                    "5.会导致人心理压抑，爱发无名之火，精神状态欠佳。\n" +
                    "6.会使人的脑供血不足，导致脑供氧和营养物质减少，加重人体乏力、失眠、记忆力减退并增大患老年性痴呆症的可能性");
            sprot.save();

            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 10);
            calendar.set(Calendar.SECOND, 0);
            Habit water = new Habit();
            water.setMainTitle("至少8杯水");
            water.setSubTitle("多喝水可以更好的维持人体的新陈代谢，更加健康");
            water.setOfficial(true);
            water.setLevel(4);
            water.setNeedSign(false);
            water.setClockTime(calendar.getTimeInMillis());
            water.setContent("多喝水的好处：\n" +
                    "1.身体内足够的水分，可以促进血液循环。\n" +
                    "2.多喝水，会有利于身体排出毒素，保证身体健康。\n" +
                    "3.美容养颜，经常多喝点白开水，这样就能让人们通过尿液的方式，然后就能把身体里面的许多毒素给清除掉，从而也能让皮肤减少色斑的出现。\n" +
                    "4.很多人因为内分泌失调，可能会脾气暴躁，其实内分泌失调也是会引发很多的生育疾病的。这个时候，如果多喝水，是可以调节的，喝水可以促进身体每个部位的循环，改善身体情况。\n" +
                    "5. 稀释痰液,缓解咳嗽。嗓子难受、有痰却咳不出来说话时间长嗓子老发痒想咳嗽，这些都让大家觉的憋气难受。我们该怎么办呢?这时最需要的就是喝水。热水的温度能很好的安抚气管，使咳嗽的频率降低。\n" +
                    "6. 促进肠动，解决便秘。便秘原因用很简单的话来讲就是体内缺失水分，因此，当我们的身体出现便秘问题时，我们可以通过喝很多很多的水来解决。");
            water.save();

            calendar.set(Calendar.HOUR_OF_DAY,18);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Habit noSmoke = new Habit();
            noSmoke.setMainTitle("戒烟");
            noSmoke.setSubTitle("吸烟影响自己及周边人健康");
            noSmoke.setContent("吸烟的危害：\n" +
                    "1. 引发心脏病\n" +
                    "香烟里面含有很多的尼古丁，而这些尼古丁会让我们的心跳出现加快，并且血压会慢慢的升高。并且烟雾里面的一氧化碳还会促进动脉粥样化出现慢慢累积的情况，而这些情况是很容易引发心脏病的。\n" +
                    "2.引发癌症\n" +
                    "香烟里面所含有的尼古丁，焦油跟一氧化碳都是具有强大的致癌性，会使得男性患癌几率升高，从而引发癌症，加重死亡的几率。\n" +
                    "3.伤害肺部\n" +
                    "吸烟的时候会产生烟雾，而这些烟雾里面可以说是含有很多的有害物质，当我们吸入这些有害物质的话，就会出现在肺部里面，而这个时候肺部就会受到污染跟伤害，最后就慢慢的伤害肺部健康。");
            noSmoke.setOfficial(true);
            noSmoke.setClockTime(calendar.getTimeInMillis());
            noSmoke.setLevel(5);
            noSmoke.setNeedSign(false);
            noSmoke.save();

            calendar.set(Calendar.HOUR_OF_DAY, 18);
            Habit noWine = new Habit();
            noWine.setLevel(6);
            noWine.setMainTitle("戒酒");
            noWine.setSubTitle("酗酒影响身体健康");
            noWine.setContent("大量饮酒的危害：\n" +
                    "1.视力减退\n" +
                    "酒中所含有的甲醇，在人体内继续分解则会转化为甲醛。这种物质对人的视网膜有很大的毒性，长期酗酒会导致视网膜持续遭受损害，从而会使视力迅速下降，甚至导致失明。\n" +
                    "2.伤害肝脏\n" +
                    "中含有酒精即乙醇，它对人体的组织器官有直接毒害作用，其中伤害最重的部位是肝脏。当酒进入人体后，首先进入肠胃，被胃吸收约20%，被肠吸收约80%，不管怎么样，最后都要通过肝脏才能被分解和处理。大约95%的酒精要通过肝脏来解毒，但肝脏分解酒精的能力是有限的，而且分解后，相当数量的酒精再肝细胞内的乙醇脱氢酶的作用下转化为对人体有害的乙醛，这样就干扰了肝脏的正常代谢。\n" +
                    "3.消化道损害\n" +
                    "酗酒之人大多喜饮烈性酒，而越是烈性酒，对消化道的刺激就越大。若消化道长期遭受酒精的刺激，则可能导致多种消化器官疾病的发生，如胃出血、胃炎、胃溃疡、食管炎和胰腺炎等。");
            noWine.setLevel(6);
            noWine.setNeedSign(false);
            noWine.setOfficial(true);
            noWine.save();


            calendar.set(Calendar.HOUR_OF_DAY, 22);
            Habit sleep = new Habit();
            sleep.setLevel(7);
            sleep.setMainTitle("早睡");
            sleep.setSubTitle("经常熬夜会让记忆力下降、抵抗力下降");
            sleep.setContent("熬夜的危害究竟有多大：\n" +
                    "1. 降低免疫力\n" +
                    "　　熬夜容易出现精神不振，出现疲劳，长期这样，身体的免疫力会大幅下降，对于备孕的成功率也大打折扣。\n" +
                    "2. 内分泌失调\n" +
                    "　　熬夜会对女性内分泌系统有损害，容易出现月经不调、月经推迟、月经量少、痛经等内分泌失调症状。\n" +
                    "3. 容易上火\n" +
                    "　　熬夜容易出现功能性紊乱的情况，出现有长痤疮、眼睛红、流鼻血等上火的症状，容易引发感冒，降低怀孕成功率。\n" +
                    "4.视力下降\n" +
                    "熬夜时最劳累的器官是眼睛，因为眼肌长时间疲劳会导致暂时性的视力下降。如果长期熬夜、劳累，可能在某次熬通宵之后，出现视力模糊、视野有阴影或视物颜色改变。\n" +
                    "5.易引发肠胃疾病\n" +
                    "胃疼、胃酸，甚至引发胃溃疡胃是身体中对时刻表比较敏感的器官，熬夜易使胃酸分泌过多而诱发胃溃疡。同时，他在熬夜时常用的烟、浓茶、咖啡对胃黏膜也是不良的刺激。");
            sleep.setOfficial(true);
            sleep.setNeedSign(true);
            sleep.setClockTime(calendar.getTimeInMillis());
            sleep.save();
        }

//////////////////////备孕模式/////////////////////////////////////////

        {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Habit wakeUp = new Habit();
            wakeUp.setMainTitle("早起");
            wakeUp.setSubTitle("精神充沛，向新的一天问好。");
            wakeUp.setLevel(1);
            wakeUp.setYouSheng(true);
            wakeUp.setOfficial(true);
            wakeUp.setNeedSign(false);
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
            weight.setYouSheng(true);
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
            habit.setYouSheng(true);
            habit.setOfficial(true);
            habit.setClockTime(calendar.getTimeInMillis());
            habit.setContent("最佳服用时间：饭后半小时服用，吸收更好。\n" +
                    "服用周期：怀孕前的3个月就必须要吃叶酸，一直吃到怀孕后的3个月底，一共是服用6个月。每天0.4毫克。\n" +
                    "不吃叶酸危害：\n" +
                    "畸形儿、先天性心脏病、兔唇等疾病的发生率将大幅变高。");
            habit.save();

            calendar.set(Calendar.HOUR_OF_DAY, 20);
            Habit sprot = new Habit();
            sprot.setMainTitle("运动");
            sprot.setSubTitle("增强身体素质，促进卵子活力。");
            sprot.setLevel(4);
            sprot.setOfficial(true);
            sprot.setYouSheng(true);
            sprot.setNeedSign(true);
            sprot.setClockTime(calendar.getTimeInMillis());
            sprot.setContent("最佳锻炼时间：至少30分钟，最佳1小时，不要超过2小时。\n" +
                    "最佳锻炼时间：至少30分钟，最佳1小时，不要超过2小时。\n" +
                    "缺乏运动的危害：\n" +
                    "1.畸形儿、先天性心脏病、兔唇等疾病的发生率将大幅变高。\n" +
                    "2.使胸腔血液不足，导致人的心、肺功能进一步降低。\n" +
                    "3.长期不动会引发全身肌肉酸痛、脖子僵硬和头痛头晕。\n" +
                    "4.容易引起肠胃蠕动减慢，消化腺分泌消化液减少，出现食欲不振等症状，加重人的腹胀、便秘、消化不良等消化系统症状。\n" +
                    "5.会导致人心理压抑，爱发无名之火，精神状态欠佳。\n" +
                    "6.会使人的脑供血不足，导致脑供氧和营养物质减少，加重人体乏力、失眠、记忆力减退并增大患老年性痴呆症的可能性");
            sprot.save();

            calendar.set(Calendar.HOUR_OF_DAY, 22);
            Habit sleep = new Habit();
            sleep.setMainTitle("早睡");
            sleep.setSubTitle("经常熬夜会让记忆力下降、抵抗力下降");
            sleep.setContent("熬夜的危害究竟有多大：\n" +
                    "1. 降低免疫力\n" +
                    "　　熬夜容易出现精神不振，出现疲劳，长期这样，身体的免疫力会大幅下降，对于备孕的成功率也大打折扣。\n" +
                    "2. 内分泌失调\n" +
                    "　　熬夜会对女性内分泌系统有损害，容易出现月经不调、月经推迟、月经量少、痛经等内分泌失调症状。\n" +
                    "3. 容易上火\n" +
                    "　　熬夜容易出现功能性紊乱的情况，出现有长痤疮、眼睛红、流鼻血等上火的症状，容易引发感冒，降低怀孕成功率。\n" +
                    "4.视力下降\n" +
                    "熬夜时最劳累的器官是眼睛，因为眼肌长时间疲劳会导致暂时性的视力下降。如果长期熬夜、劳累，可能在某次熬通宵之后，出现视力模糊、视野有阴影或视物颜色改变。\n" +
                    "5.易引发肠胃疾病\n" +
                    "胃疼、胃酸，甚至引发胃溃疡胃是身体中对时刻表比较敏感的器官，熬夜易使胃酸分泌过多而诱发胃溃疡。同时，他在熬夜时常用的烟、浓茶、咖啡对胃黏膜也是不良的刺激。");
            sleep.setOfficial(true);
            sleep.setNeedSign(true);
            sleep.setYouSheng(true);
            sleep.setLevel(5);
            sleep.setClockTime(calendar.getTimeInMillis());
            sleep.save();


            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 10);
            calendar.set(Calendar.SECOND, 0);
            Habit water = new Habit();
            water.setMainTitle("至少8杯水");
            water.setSubTitle("可以在备孕期起到预防便秘的作用");
            water.setOfficial(true);
            water.setLevel(6);
            water.setYouSheng(true);
            water.setNeedSign(false);
            water.setClockTime(calendar.getTimeInMillis());
            water.setContent("建议饮水计量：每天至少八杯水，大约1500ml\n" +
                    "多喝水的好处：\n" +
                    "1.身体内足够的水分，可以促进血液循环，这样是可以提高卵子的质量。\n" +
                    "2.多喝水会促进宫颈粘液的分泌，这样会有利于精子和卵子的结合。\n" +
                    "3.多喝水，会有利于身体排出毒素，保证身体健康，有利于身体的受孕。\n" +
                    "4.孕期很多人会出现身体的负重，这个时候血容量也会增加。这个时候，为了孕期的监看，我们就要多喝水，这样可以确保身体内血液循环流畅。\n" +
                    "5.在怀孕的时候，子宫内膜对胎儿成长所需要的营养，起到的是一个缓冲的作用，这个时候，多喝水，会保证整个过程顺利进行。\n" +
                    "6.很多人因为内分泌失调，可能会脾气暴躁，其实内分泌失调也是会引发很多的生育疾病的。这个时候，如果多喝水，是可以调节的，喝水可以促进身体每个部位的循环，改善身体情况。\n" +
                    "7.如果你是一位女性，为了生完孩子之后奶水可以很充足，你更要多喝水。适当的水分，是可以促进乳汁的增多的，这不是一天两天的功夫，一定要坚持。\n" +
                    "8.当女人怀孕的时候，肝脏功能正常可以避免肾脏疾病还有肝脏受损的情况，多喝水就可以完善肾脏，避免这两种疾病的威胁。");
            water.save();


            Habit ca = new Habit();
            ca.setMainTitle("补钙");
            ca.setSubTitle("所需的钙为每天800毫克左右");
            ca.setContent("钙的作用：\n" +
                    "钙是形成骨骼与牙齿的主要成分，是胎儿发育过程中不可缺少，而且用量较多的一种物质。含钙高的食物有牛奶、豆制品、禽蛋、海产品、骨头汤等。\n" +
                    "怀孕之后孕妇身体里现有的钙质，会大量转移到宝宝的身体里，满足胎儿骨骼的发育需要，消耗的钙量要远远大于普通人，即便是不缺钙在备孕期间也要注意补钙。 \n" +
                    "应从准备怀孕的时候就开始补钙，所需的钙为每天800毫克左右。");
            ca.setYouSheng(true);
            ca.setClockTime(calendar.getTimeInMillis());
            ca.setLevel(7);
            ca.setNeedSign(false);
            ca.setOfficial(true);
            ca.save();

            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Habit noSmoke = new Habit();
            noSmoke.setMainTitle("戒烟");
            noSmoke.setYouSheng(true);
            noSmoke.setSubTitle("吸引对男性、孕妇都有严重影响");
            noSmoke.setContent("吸烟的危害：\n" +
                    "1.吸烟可损害精子质量使精子数目减少、活力下降，导致男性不育。\n" +
                    "2.育龄女性吸烟或被动吸烟可影响卵子质量而引起不孕。怀孕妇女吸烟会引起死胎、流产和胎儿畸形。\n" +
                    "3. 男性朋友要在计划怀孕之前的三个月甚至半年之内戒烟戒酒，因为吸烟会引发性功能障碍，降低生育力能，影响到受孕的成功率，影响受精卵及胚胎的质量");
            noSmoke.setOfficial(true);
            noSmoke.setClockTime(calendar.getTimeInMillis());
            noSmoke.setLevel(8);
            noSmoke.setNeedSign(false);
            noSmoke.save();


            calendar.set(Calendar.HOUR_OF_DAY, 18);
            Habit noWine = new Habit();
            noWine.setYouSheng(true);
            noWine.setMainTitle("戒酒");
            noWine.setSubTitle("大量饮酒可能导致畸形胎儿、胎儿发育迟缓等");
            noWine.setContent("大量饮酒的危害：\n" +
                    "1.女性备孕期间饮酒过度并且受孕的话，很可能导致畸形胎儿、死胎、胎儿发育迟缓，或中枢神经系统异常等情况出现。\n" +
                    "2.男性长期大量饮酒，会导致男性的精子数量减少，活力降低，畸形精子，影响受孕和胚胎发育。");
            noWine.setLevel(9);
            noWine.setNeedSign(false);
            noWine.setOfficial(true);
            noWine.setClockTime(calendar.getTimeInMillis());
            noWine.save();


            calendar.set(Calendar.HOUR_OF_DAY, 9);
            Habit noCoffee = new Habit();
            noCoffee.setMainTitle("不喝咖啡");
            noCoffee.setSubTitle("咖啡因可导致形成乳房和卵巢囊肿");
            noCoffee.setContent("备孕/怀孕期间喝咖啡的危害：\n" +
                    "咖啡因可以加快良性乳腺疾病的发展。对于多囊卵巢综合征、子宫肌瘤、子宫内膜异位症、卵巢囊肿和纤维囊性乳房的妇女来说，咖啡因是促进囊肿形成的原因。");
            noCoffee.setYouSheng(true);
            noCoffee.setOfficial(true);
            noCoffee.setLevel(10);
            noCoffee.setClockTime(calendar.getTimeInMillis());
            noCoffee.setNeedSign(false);
            noCoffee.save();
        }

    }

    private void insertMarketData() {
        if (LitePal.count(Market.class) > 0)
            return;

        {

            Market huaWei = new Market();
            huaWei.setId(1);
            huaWei.setName("智汇云（华为）");
            huaWei.setPackageName("com.huawei.appmarket");
            huaWei.save();

            Market baidu = new Market();
            baidu.setId(2);
            baidu.setName("百度手机助手");
            baidu.setPackageName("com.baidu.appsearch");
            baidu.save();

            Market tencent = new Market();
            tencent.setId(3);
            tencent.setName("应用宝");
            tencent.setPackageName("com.tencent.android.qqdownloader");
            tencent.save();

            Market qihoo = new Market();
            qihoo.setId(4);
            qihoo.setName("360");
            qihoo.setPackageName("com.qihoo.appstore");
            qihoo.save();

            Market mi = new Market();
            mi.setId(5);
            mi.setName("小米应用商店");
            mi.setPackageName("com.xiaomi.market");
            mi.save();

            Market oppo = new Market();
            oppo.setId(6);
            oppo.setName("OPPO应用商店");
            oppo.setPackageName("com.oppo.market");
            oppo.save();

            Market vivo = new Market();
            vivo.setId(7);
            vivo.setName("VIVO应用商店");
            vivo.setPackageName("com.bbk.appstore");
            vivo.save();

            Market wdj = new Market();
            wdj.setId(8);
            wdj.setName("豌豆荚");
            wdj.setPackageName("com.wandoujia.phoenix2");
            wdj.save();

            Market az = new Market();
            az.setId(9);
            az.setName("安智");
            az.setPackageName("cn.goapk.market");
            az.save();

            Market lenovo = new Market();
            lenovo.setId(10);
            lenovo.setName("联想乐商店");
            lenovo.setPackageName("com.lenovo.leos.appstore");
            lenovo.save();

            Market cool = new Market();
            cool.setId(11);
            cool.setName("酷安");
            cool.setPackageName("com.coolapk.market");
            cool.save();

        }
    }
}
