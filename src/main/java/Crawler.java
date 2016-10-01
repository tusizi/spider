import constant.Constant;
import parse.ChannelParser;

public class Crawler {

    public static void main(String[] args) {
        new Crawler().start();
    }

    public void start() {
        for (String name : Constant.CHANNELS) {
            System.out.println("start get channel : " + name);
            new ChannelParser(name).run();
//            ThreadPool.threadPoolExecutor.submit(new ChannelParser(name));
        }
    }
}

