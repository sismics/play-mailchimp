package helpers.api.mailchimp.service;

import com.google.gson.JsonObject;
import helpers.api.mailchimp.MailChimp;
import helpers.api.mailchimp.model.Member;
import okhttp3.Request;
import okhttp3.RequestBody;
import play.Logger;

import java.util.Map;

/**
 * @author jtremeaux
 */
public class ListService {
    public MailChimp mailChimp;

    public ListService(MailChimp mailChimp) {
        this.mailChimp = mailChimp;
    }

    public void addSubscriber(Member member) {
        JsonObject data = new JsonObject();
        data.addProperty("email_address", member.emailAddress);
        data.addProperty("status", "subscribed");
        if (!member.mergeFields.isEmpty()) {
            JsonObject mergeFields = new JsonObject();
            for (Map.Entry<String, String> entry : member.mergeFields.entrySet()) {
                mergeFields.addProperty(entry.getKey(), entry.getValue());
            }
            data.add("merge_fields", mergeFields);
        }
        if (member.language != null) {
            data.addProperty("language", member.language);
        }

        Request request = mailChimp.authenticate(new Request.Builder()
                .url("https://" + mailChimp.getDc() + ".api.mailchimp.com/3.0/lists/" + mailChimp.getListId() + "/members")
                .post(RequestBody.create(mailChimp.JSON, data.toString()))
                .build());
        mailChimp.execute(request,
                (response) -> {
                    Logger.debug("Added user to MailChimp: " + member.emailAddress);
                    return null;
                }, (response) -> {
                    Logger.error("Error adding user to MailChimp, response was: " + response.body().string());
                });
    }
}
