import parse.SearchParser;

public class Crawler {

    public static void main(String[] args) {
        new Crawler().start();
    }

    public void start() {
//        for (String name : Constant.CHANNELS) {
//            System.out.println("start getChannel channel : " + name);
//            new ChannelParser(name).run();
////            ThreadPool.threadPoolExecutor.submit(new ChannelParser(name));
//        }

        new SearchParser("房价", 0, 30).run();
    }
}

