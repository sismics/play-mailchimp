package helpers.api.mailchimp;

import com.google.common.base.Charsets;
import com.sismics.sapparot.function.CheckedConsumer;
import com.sismics.sapparot.function.CheckedFunction;
import com.sismics.sapparot.okhttp.OkHttpHelper;
import helpers.api.mailchimp.mock.MockListService;
import helpers.api.mailchimp.service.ListService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import play.Play;

/**
 * @author jtremeaux
 */
public class MailChimp {
    private static MailChimp instance;

    private OkHttpClient client;

    private ListService listService;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static MailChimp get() {
        if (instance == null) {
            instance = new MailChimp();
        }
        return instance;
    }

    public MailChimp() {
        client = createClient();
        if (isMock()) {
            listService = MockListService.create();
        } else {
            listService = new ListService(this);
        }
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    public boolean isMock() {
        return Boolean.parseBoolean(Play.configuration.getProperty("mailchimp.mock", "false"));
    }

    public String getListId() {
        return Play.configuration.getProperty("mailchimp.listId");
    }

    public String getDc() {
        return getApiKey().split("-")[1];
    }

    public String getApiKey() {
        return Play.configuration.getProperty("mailchimp.apiKey");
    }

    public ListService getListService() {
        return listService;
    }

    public Request authenticate(Request request) {
        return request.newBuilder()
                .addHeader("Authorization", "Basic " + Base64.encodeBase64String(("mc:" + getApiKey()).getBytes(Charsets.UTF_8)))
                .build();
    }

    public <T> T execute(Request request, CheckedFunction<Response, T> onSuccess, CheckedConsumer<Response> onFailure) {
        return OkHttpHelper.execute(client, request, onSuccess, onFailure);
    }
}
