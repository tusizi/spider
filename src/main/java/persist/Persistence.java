package persist;

import cache.Cache;
import com.google.gson.Gson;
import entity.Article;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Persistence implements Runnable {
    public Persistence(Article article) {
        this.article = article;
    }

    private Article article;

    public static void persist(Article article) {
        String date = article.getDate();
        try {
            Date parse = new SimpleDateFormat("yy-MM-dd HH:mm").parse(date);
            String simpleDate = new SimpleDateFormat("yyMMdd").format(parse);

            String dir = System.getProperty("user.dir") + File.separator + "data";
            makeDirs(dir);
            makeDirs(dir + File.separator + simpleDate);
            File file = new File(dir + File.separator + simpleDate + File.separator + article.getId());
            if (!file.exists()) {
                boolean contains = Cache.fileCounts.contains(article.getId());
                if (!contains) {
                    Cache.fileCounts.add(article.getId());
                    if (Cache.fileCounts.size() % 10 == 0)
                        System.out.println(Cache.fileCounts.size());
                }
                file.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(file, false);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            String str = new Gson().toJson(article);
            bufferWritter.write(str);
            bufferWritter.write("\n");
            bufferWritter.close();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static boolean makeDirs(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File folder = new File(filePath);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    public void run() {
        persist(article);
    }
}
