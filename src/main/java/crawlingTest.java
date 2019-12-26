import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class crawlingTest {

    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    public static void main(String[] args){
        Connection.Response response = null;
        try {
            //파일 객체 생성
            Path path = Paths.get("urlList.txt");
            Charset cs = StandardCharsets.UTF_8;
            List<String> list = new ArrayList<String>();
            list = Files.readAllLines(path,cs);

            for(int i = 0; i<list.size(); ++i){
                // 1. URL 선언
                String connUrl = list.get(i);

                // 2. HTML 가져오기
                response = Jsoup
                        .connect(connUrl)
                        .userAgent(USER_AGENT)
                        .method(Connection.Method.GET)
                        .execute();

                // html 파일로 쓰기
                File file = new File("crawlingFiles/"+(i+1)+".html");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

                Document doc = response.parse();
                // 3. 가져온 HTML Document 를 확인하기
                System.out.println(doc.toString());
                if(response.statusCode() != 200){
                    if(file.isFile() && file.canWrite()){
                        bufferedWriter.write("FAILED");
                        bufferedWriter.close();
                        break;
                    }
                }

                if(file.isFile() && file.canWrite()){
                    bufferedWriter.write(doc.toString());
                    bufferedWriter.close();
                }
            }


        } catch (IOException e) {
            // Exp : Connection Fail
            e.printStackTrace();
        }

    }
}
