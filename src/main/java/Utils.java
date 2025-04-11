import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Utils {
   public static String getUrl(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();

        CloseableHttpResponse response = null;
        Nasa answer = null;
        try {
            response = client.execute(new HttpGet(url));
            answer = mapper.readValue(response.getEntity().getContent(), Nasa.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return answer.url;
    }
}
