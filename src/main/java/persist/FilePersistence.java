package persist;

import com.google.gson.Gson;
import entity.Article;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FilePersistence implements Persistence {
    private static Set<String> ids = new HashSet<String>();
    private static Set<String> fileCounts = new HashSet<String>();
    private static Set<String> channels = new HashSet<String>();

    public static boolean makeDirs(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File folder = new File(filePath);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    public void saveArticle(Article article) {
        String date = article.getDate();
        try {
            Date parse = new SimpleDateFormat("yy-MM-dd HH:mm").parse(date);
            String simpleDate = new SimpleDateFormat("yyMMdd").format(parse);

            String dir = System.getProperty("user.dir") + File.separator + "data";
            makeDirs(dir);
//            makeDirs(dir + File.separator + simpleDate);
            File file = new File(dir + File.separator + simpleDate);
            FilePersistence.ids.add(article.getId());
            if (!file.exists()) {
                boolean contains = fileCounts.contains(article.getId());
                if (!contains) {
                    fileCounts.add(article.getId());
                    if (fileCounts.size() % 10 == 0)
                        System.out.println(fileCounts.size());
                }
                file.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(file, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            String str = new Gson().toJson(article);
            bufferWritter.append(str);
            bufferWritter.append("\n");
            bufferWritter.close();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public int articleCount() {
        return ids.size();
    }

    public boolean isChannelExist(String id) {
        return channels.contains(id);
    }

    public boolean isArticleExist(String id) {
        return ids.contains(id);
    }

    public void saveChannel(String id) {
        channels.add(id);
    }
}
