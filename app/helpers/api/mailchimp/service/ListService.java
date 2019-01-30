package helpers.api.mailchimp.service;

import com.google.gson.JsonObject;
import helpers.api.mailchimp.MailChimp;
import okhttp3.Request;
import okhttp3.RequestBody;
import play.Logger;

/**
 * @author jtremeaux
 */
public class ListService {
    public MailChimp mailChimp;

    public ListService(MailChimp mailChimp) {
        this.mailChimp = mailChimp;
    }

    /**
     * Add a subscriber to a list.
     *
     * @param email The email to add
     */
    public void addSubscriber(String email) {
        JsonObject data = new JsonObject();
        data.addProperty("email_address", email);
        data.addProperty("status", "subscribed");

        Request request = mailChimp.authenticate(new Request.Builder()
                .url("https://" + mailChimp.getDc() + ".api.mailchimp.com/3.0/lists/" + mailChimp.getListId() + "/members")
                .post(RequestBody.create(mailChimp.JSON, data.toString()))
                .build());
        mailChimp.execute(request,
                (response) -> {
                    Logger.debug("Added user to MailChimp: " + email);
                    return null;
                }, (response) -> {
                    Logger.error("Error adding user to MailChimp, response was: " + response.body().string());
                });
    }
}
