package fingerVisit

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

/**
 * Created by xiwen.yxw on 2017/1/17.
 */
public class FingerVisit {

    private String queryAudioAfp(byte[] buffer) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://finger.xiami.com/s/single_search?len=768");
        HttpEntity entity = new ByteArrayEntity(buffer);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);

        HttpEntity resEntity = response.getEntity();
        String json = EntityUtils.toString(resEntity);
        return json
    }


    String dealEachFile(File file) {
        byte[] buffer = new byte[768];
        RandomAccessFile f = new RandomAccessFile(file, "r");
        long fileLength = f.length();
        if (fileLength % 4 != 0) {
            return null;
        }
        int off = (int)((fileLength/4)/8)*4;
        f.seek(off);
        f.read(buffer, 0, buffer.length);
        String json1 = queryAudioAfp(buffer);
        System.out.println("json1 = " + json1 + " | off = " + off);

        off = (int)((fileLength/4)/2)*4;
        f.seek(off);
        f.read(buffer, 0, buffer.length);
        String json2 = queryAudioAfp(buffer);
        System.out.println("json2 = " + json2 + " | off = " + off);

        off = (int)((fileLength/4)/8)*6*4;
        f.seek(off);
        f.read(buffer, 0, buffer.length);
        String json3 = queryAudioAfp(buffer);
        System.out.println("json3 = " + json3 + " | off = " + off);
        return null;
    }

    public void mainLoop(String path) {
        File file = new File(path);
        System.out.println("file name = " + file.getName());
        File[] fileArr = file.listFiles();
        System.out.println("len = " + fileArr.length);
        for (File item : file.listFiles()) {
            System.out.println("name = " + item.getName());
            dealEachFile(item);
        }
    }

    public static void main(String[] args) {

    }
}